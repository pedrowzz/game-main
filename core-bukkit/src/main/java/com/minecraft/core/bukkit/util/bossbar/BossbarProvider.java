package com.minecraft.core.bukkit.util.bossbar;

import com.minecraft.core.bukkit.util.bossbar.interfaces.BossbarHandler;
import com.minecraft.core.bukkit.util.bossbar.util.NMS;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.imanity.imanityspigot.movement.MovementHandler;
import org.imanity.imanityspigot.packet.wrappers.MovementPacketWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BossbarProvider implements Listener, BossbarHandler, MovementHandler {

    static {
        NMS.registerCustomEntity("WitherBoss", BossbarWither.class, 64);
    }

    private final Plugin plugin;
    private final Map<UUID, Bossbar> spawnedWithers = new HashMap<>();

    public BossbarProvider(Plugin plugin) {
        this.plugin = plugin;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        new BukkitRunnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateBossbar(player);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);

        Bukkit.imanity().registerMovementHandler(plugin, this);

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        clearBossbar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        clearBossbar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        teleport(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        teleport(event.getPlayer());
    }

    private void teleport(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            updateBossbar(player);
            updateLocation(player, player.getLocation());
        }, 2L);
    }

    private Bossbar newBossbar() {
        return newBossbar(ChatColor.BOLD + "", 1f);
    }

    private Bossbar newBossbar(String message, float percentage) {
        return new CraftWitherBossbar(message, null).setMessage(message).setPercentage(percentage);
    }

    public void clearBossbar(Player player) {
        Bossbar bossbar = spawnedWithers.remove(player.getUniqueId());

        if (bossbar == null || !bossbar.isSpawned()) {
            return;
        }

        bossbar.setSpawned(false);
        NMS.sendPacket(player, bossbar.getDestroyPacket());
    }

    public Bossbar getBossbar(Player player) {
        Bossbar bossbar = spawnedWithers.get(player.getUniqueId());
        if (bossbar == null) {
            bossbar = newBossbar();
            spawnedWithers.put(player.getUniqueId(), bossbar);
        }
        return bossbar;
    }

    public boolean hasBossbar(Player player) {
        return spawnedWithers.containsKey(player.getUniqueId());
    }

    public void updateBossbar(Player player) {
        Bossbar bossbar = spawnedWithers.get(player.getUniqueId());

        if (bossbar == null)
            return;

        if (bossbar.isExpired()) {
            clearBossbar(player);
            return;
        }

        bossbar.decreaseDuration();

        if (!bossbar.isSpawned()) {
            bossbar.setSpawned(true);

            Location loc = player.getLocation();

            bossbar.setSpawnLocation(loc.getDirection().multiply(20).add(loc.toVector()).toLocation(player.getWorld()));
            NMS.sendPacket(player, bossbar.getSpawnPacket());
        }

        NMS.sendPacket(player, bossbar.getMetaPacket(bossbar.getWatcher()));
    }

    public void updateLocation(Player player, Location location) {

        Bossbar bossbar = spawnedWithers.get(player.getUniqueId());

        if (bossbar == null) {
            return;
        }

        if (!bossbar.isSpawned()) {
            return;
        }

        NMS.sendPacket(player, bossbar.getTeleportPacket(location.getDirection().multiply(20).add(location.toVector()).toLocation(bossbar.spawnLocation.getWorld())));
    }

    @Override
    public void onUpdateLocation(Player player, Location location, Location location1, MovementPacketWrapper movementPacketWrapper) {
        if (!hasBossbar(player))
            return;

        updateLocation(player, location1);
    }

    @Override
    public void onUpdateRotation(Player player, Location location, Location location1, MovementPacketWrapper movementPacketWrapper) {
        if (!hasBossbar(player))
            return;

        updateLocation(player, location1);
    }
}
