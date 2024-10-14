package com.minecraft.arcade.pvp.game;

import com.minecraft.arcade.pvp.PvP;
import com.minecraft.arcade.pvp.game.util.GameConfiguration;
import com.minecraft.arcade.pvp.kit.Kit;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.arcade.pvp.util.Assistance;
import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.vanish.Vanish;
import com.minecraft.core.bukkit.util.variable.VariableStorage;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.github.paperspigot.Title;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class Game implements Listener, Assistance, VariableStorage, BukkitInterface {

    private final String name = getClass().getSimpleName();
    private int id;

    private GameType type;
    private World world;

    private final GameConfiguration configuration = new GameConfiguration(this);

    private final Set<User> users = new HashSet<>();

    public abstract void handleSidebar(User user);

    public abstract void onSpawn(User user);

    public Set<User> getPlayingUsers() {
        return users.stream().filter(user -> !Vanish.getInstance().isVanished(user.getAccount().getUniqueId())).collect(Collectors.toSet());
    }

    public void sendMessage(final String message) {
        this.world.getPlayers().forEach(player -> player.sendMessage(message));
    }

    public void sendTitle(final Title title) {
        this.world.getPlayers().forEach(player -> player.sendTitle(title));
    }

    public void playSound(final Sound sound, final float var, final float var1) {
        this.world.getPlayers().forEach(player -> player.playSound(player.getLocation(), sound, var, var1));
    }

    public void onPlayerJoinEvent(final User user, final boolean teleport) {
        this.users.add(user);

        final Player player = user.getPlayer();

        if (player.getPassenger() != null)
            player.getPassenger().leaveVehicle();

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setItemOnCursor(null);
        player.getOpenInventory().getTopInventory().clear();
        player.setLevel(0);
        player.setExp(0);

        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

        player.setGameMode(GameMode.SURVIVAL);

        user.getCombatTag().resetTag();

        for (Kit kit : user.getKits()) {
            kit.removeCooldown(player);
        }

        if (teleport) player.teleport(user.getGame().getConfiguration().getSpawn());
    }

    public void onPlayerQuitEvent(final User user) {
        this.users.remove(user);
    }

    private final PvP instance = PvP.getInstance();

}