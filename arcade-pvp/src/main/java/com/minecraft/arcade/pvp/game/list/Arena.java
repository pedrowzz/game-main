package com.minecraft.arcade.pvp.game.list;

import com.minecraft.arcade.pvp.event.user.LivingUserDieEvent;
import com.minecraft.arcade.pvp.event.user.LivingUserLostProtectionEvent;
import com.minecraft.arcade.pvp.game.Game;
import com.minecraft.arcade.pvp.kit.Kit;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.bukkit.arcade.map.area.Cuboid;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.bukkit.util.scoreboard.GameScoreboard;
import com.minecraft.core.database.data.DataStorage;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.core.database.enums.Tables;
import com.minecraft.arcade.pvp.util.EventUtils;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.imanity.imanityspigot.movement.MovementHandler;
import org.imanity.imanityspigot.packet.wrappers.MovementPacketWrapper;

import java.util.*;

@Getter
public class Arena extends Game implements MovementHandler {

    protected final Cuboid cuboid;
    private final Set<UUID> protectedUuidSet;

    public Arena() {
        setId(1);
        setType(GameType.ARENA);
        setWorld(Bukkit.getWorld("arena"));

        this.protectedUuidSet = new HashSet<>();

        this.cuboid = new Cuboid(new Location(this.getWorld(), 24, 114, 7), new Location(this.getWorld(), -22, 140, -7));
        this.getInstance().getServer().imanity().registerMovementHandler(this.getInstance(), this);

        this.getConfiguration().addColumns(Columns.PVP_ARENA_KILLS, Columns.PVP_ARENA_DEATHS, Columns.PVP_ARENA_KILLSTREAK, Columns.PVP_ARENA_MAX_KILLSTREAK);
    }

    @Override
    public void handleSidebar(User user) {
        final GameScoreboard gameScoreboard = user.getScoreboard();

        if (gameScoreboard == null)
            return;

        gameScoreboard.updateTitle("§b§lARENA");

        DataStorage dataStorage = user.getAccount().getDataStorage();

        Collection<String> lines = new ArrayList<>();

        lines.add("§fKills: §7" + dataStorage.getData(Columns.PVP_ARENA_KILLS).getAsInteger());
        lines.add("§fDeaths: §7" + dataStorage.getData(Columns.PVP_ARENA_DEATHS).getAsInteger());
        lines.add(" ");

        Iterator<Kit> iterator = user.getKitList().stream().filter(kit -> !kit.isNone()).iterator();
        int kitCount = 0;

        if (iterator.hasNext())
            lines.add(" ");

        while (iterator.hasNext()) {

            Kit kit = iterator.next();

            if (iterator.hasNext() || kitCount != 0) {
                kitCount++;
                lines.add("§fKit " + kitCount + ": §a" + kit.getDisplayName());
            } else
                lines.add("§fKit: §a" + kit.getDisplayName());
        }

        lines.add("§fKillstreak: §a" + dataStorage.getData(Columns.PVP_ARENA_KILLSTREAK).getAsInteger());
        lines.add(" ");
        lines.add("§fCoins: §6" + dataStorage.getData(Columns.PVP_COINS).getAsInteger());
        lines.add(" ");
        lines.add("§e" + Constants.SERVER_WEBSITE);

        gameScoreboard.updateLines(lines);
    }

    @EventHandler
    public void onDeath(final LivingUserDieEvent event) {

        final User killed = event.getKilled();

        if (killed.getGame().getId() != this.getId())
            return;

        if (event.hasKiller()) {
            final User killer = event.getKiller();

            if (killer.getGame().getId() != this.getId())
                return;

            killed.getPlayer().sendMessage(killed.getAccount().getLanguage().translate("pvp.arena.death_to_player", killer.getPlayer().getName()));
            killer.getPlayer().sendMessage(killer.getAccount().getLanguage().translate("pvp.arena.kill", killed.getPlayer().getName()));

            killer.getPlayer().playSound(killer.getPlayer().getLocation(), Sound.ORB_PICKUP, 4F, 4F);

            final List<ItemStack> drops = new ArrayList<>(event.getDrops());
            event.getDrops().clear();

            drops.forEach(item -> killer.getPlayer().getWorld().dropItemNaturally(killer.getPlayer().getLocation().add(0, 0.2, 0), item));

            verifyLostKillstreak(killed, killer, killed.getAccount().getData(Columns.PVP_ARENA_KILLSTREAK).getAsInt());

            killer.getCombatTag().resetTag();
            killer.giveCoins(10);

            killer.getAccount().addInt(1, Columns.PVP_ARENA_KILLS);
            killer.getAccount().addInt(1, Columns.PVP_ARENA_KILLSTREAK);

            killed.getAccount().addInt(1, Columns.PVP_ARENA_DEATHS);
            killed.getAccount().getData(Columns.PVP_ARENA_KILLSTREAK).setData(0);

            if (killer.getAccount().getData(Columns.PVP_ARENA_KILLSTREAK).getAsInt() > killer.getAccount().getData(Columns.PVP_ARENA_MAX_KILLSTREAK).getAsInt())
                killer.getAccount().getData(Columns.PVP_ARENA_MAX_KILLSTREAK).setData(killer.getAccount().getData(Columns.PVP_ARENA_KILLSTREAK).getAsInt());

            //EXP STUFF

            verifyReceiveKillstreak(killer, killer.getAccount().getData(Columns.PVP_ARENA_KILLSTREAK).getAsInt());

            for (final Kit kit : killer.getKits()) {
                kit.appreciate(event);
            }

            this.handleSidebar(killer);
        } else {
            killed.getPlayer().sendMessage(killed.getAccount().getLanguage().translate("pvp.arena.death_to_anyone"));

            verifyLostKillstreak(killed, null, killed.getAccount().getData(Columns.PVP_ARENA_KILLSTREAK).getAsInt());

            killed.getAccount().addInt(1, Columns.PVP_ARENA_DEATHS);
            killed.getAccount().getData(Columns.PVP_ARENA_KILLSTREAK).setData(0);

            //EXP STUFF
        }

        if (event.getDieCause() == LivingUserDieEvent.DieCause.KILL)
            onPlayerJoinEvent(killed, true);

        async(() -> {
            killed.getAccount().getDataStorage().saveTable(Tables.PVP);
            if (event.hasKiller()) event.getKiller().getAccount().getDataStorage().saveTable(Tables.PVP);
        });
    }

    @Override
    public void onSpawn(User user) {
        final Account account = user.getAccount();

        if (this.protectedUuidSet.contains(account.getUniqueId())) {
            user.getPlayer().sendMessage(account.getLanguage().translate("command.spawn.already_in"));
            return;
        }

        this.onPlayerJoinEvent(user, true);
    }

    @EventHandler
    public void onLostProtection(final LivingUserLostProtectionEvent event) {
        final User user = event.getUser();
        final Player player = user.getPlayer();

        player.sendMessage(user.getAccount().getLanguage().translate("pvp.lost_protection"));

        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);

        player.setItemOnCursor(null);
        player.getOpenInventory().getTopInventory().clear();

        inventory.setItem(0, sword.clone());

        inventory.setItem(0, compass.clone());

        inventory.setItem(13, bowl.clone());
        inventory.setItem(14, red_mushroom.clone());
        inventory.setItem(15, brown_mushroom.clone());

        for (Kit kit : user.getKits()) {
            kit.giveItems(inventory);
        }

        for (int i = 0; i < 31; i++)
            inventory.addItem(mushroom_soup.clone());

        player.updateInventory();

        this.handleSidebar(user);
    }

    protected void verifyProtection(final Player player) {
        final World world = player.getWorld();

        if (!world.hasMetadata("game")) {
            return;
        }

        final Game game = (Game) world.getMetadata("game").get(0).value();

        if (game.getId() != this.getId()) {
            return;
        }

        if (this.protectedUuidSet.contains(player.getUniqueId()))
            return;

        if (this.cuboid.isOutside(player.getLocation())) {
            this.protectedUuidSet.remove(player.getUniqueId());

            new LivingUserLostProtectionEvent(User.fetch(player.getUniqueId())).fire();
        }
    }

    @Override
    public void onPlayerJoinEvent(User user, boolean teleport) {
        super.onPlayerJoinEvent(user, teleport);

        for (Kit kit : user.getKits()) {
            kit.resetAttributes(user);
        }

        this.protectedUuidSet.add(user.getAccount().getUniqueId());
    }

    @Override
    public void onPlayerQuitEvent(User user) {
        super.onPlayerQuitEvent(user);

        this.protectedUuidSet.remove(user.getAccount().getUniqueId());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (this.protectedUuidSet.contains(event.getEntity().getUniqueId()) && event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (this.protectedUuidSet.contains(event.getEntity().getUniqueId()) && EventUtils.isBothPlayers(event))
            event.setCancelled(true);
    }

    protected final ItemStack sword = addTag(new ItemFactory(Material.STONE_SWORD).setUnbreakable().getStack(), "undroppable");

    protected final ItemStack compass = addTag(new ItemStack(Material.COMPASS), "undroppable");

    protected final ItemStack bowl = new ItemStack(Material.BOWL, 25);
    protected final ItemStack red_mushroom = new ItemStack(Material.RED_MUSHROOM, 25);
    protected final ItemStack brown_mushroom = new ItemStack(Material.BROWN_MUSHROOM, 25);

    protected final ItemStack mushroom_soup = new ItemStack(Material.MUSHROOM_SOUP);

    @Override
    public void onUpdateLocation(Player player, Location location, Location location1, MovementPacketWrapper movementPacketWrapper) {
        this.verifyProtection(player);
    }

    @Override
    public void onUpdateRotation(Player player, Location location, Location location1, MovementPacketWrapper movementPacketWrapper) {
        this.verifyProtection(player);
    }

    protected void verifyReceiveKillstreak(User user, int killstreak) {
        if (killstreak < 9 || killstreak % 10 != 0)
            return;
        user.getGame().getUsers().forEach(users -> users.getPlayer().sendMessage("§b" + user.getAccount().getDisplayName() + "§e atingiu um killstreak de §6" + killstreak + "§e!"));
    }

    protected void verifyLostKillstreak(User user, User killer, int killstreak) {
        if (killstreak < 9)
            return;
        user.getGame().getUsers().forEach(users -> users.getPlayer().sendMessage("§b" + user.getAccount().getDisplayName() + "§e perdeu um killstreak de §6" + killstreak + (killer == null ? "§e!" : " §epara §b" + killer.getAccount().getDisplayName() + "§e!")));
    }

}