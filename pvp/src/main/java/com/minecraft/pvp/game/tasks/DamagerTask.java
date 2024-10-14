package com.minecraft.pvp.game.tasks;

import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.scheduler.GameRunnable;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.pvp.PvP;
import com.minecraft.pvp.event.UserDiedEvent;
import com.minecraft.pvp.game.Game;
import com.minecraft.pvp.util.DamageSettings;
import com.minecraft.pvp.util.GameMetadata;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class DamagerTask extends GameRunnable implements BukkitInterface {

    private final Game game;

    public DamagerTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        tick++;

        boolean isNewSecond = tick % 20 == 0;
        boolean needToDrop = tick % 100 == 0;

        if (tick % 10 == 0) {
            this.game.getUsers().forEach(user -> {
                if (user.isKept())
                    return;

                PvP.getPvP().getVisibility().update();

                DamageSettings damageSettings = user.getDamageSettings();

                if (tick % damageSettings.getFrequency().getFrequency() == 0) {
                    Player player = user.getPlayer();

                    double damage = damageSettings.isInChallenge() ? damageSettings.getChallenge().getDamage() : damageSettings.getType().getDamage();

                    if (player.getHealth() - damage <= 0) {

                        if (damageSettings.isInChallenge()) {
                            Account account = Account.fetch(player.getUniqueId());

                            Columns columns = damageSettings.getChallenge().getColumns();
                            boolean record = user.getAliveSeconds() > account.getData(columns).getAsInt();

                            if (record)
                                player.sendMessage("§aParabéns! Você atingiu o seu recorde.");
                            player.sendMessage("§7Tempo: §f" + format(user.getAliveSeconds()));

                            if (record) {
                                account.getData(columns).setData(user.getAliveSeconds());
                                async(() -> account.getDataStorage().saveColumn(columns));
                            }
                        }

                        new UserDiedEvent(user, null, null, player.getLocation(), UserDiedEvent.Reason.KILL, user.getGame()).fire();
                    } else {
                        player.damage(damage);
                    }
                }

                if (isNewSecond) {
                    user.incrementSecondsAlive();
                    getGame().handleSidebar(user);
                }

                if (needToDrop && damageSettings.isDrops() && !user.isKept()) {
                    for (int i = 1; i < 6; i++) {
                        Item item = user.getGame().getWorld().dropItemNaturally(user.getPlayer().getLocation().clone().add(0, 0.5, 0), new ItemStack(Material.WOOD_SPADE));
                        item.setMetadata("owner", new GameMetadata(user.getPlayer().getEntityId()));
                    }
                }

            });
        }
    }

    protected int tick;

    public void start(Plugin plugin) {
        this.runTaskTimer(plugin, 1L, 1L);
    }

    public Game getGame() {
        return game;
    }

}