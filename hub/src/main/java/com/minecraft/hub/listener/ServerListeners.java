package com.minecraft.hub.listener;

import com.minecraft.core.bukkit.event.server.ServerHeartbeatEvent;
import com.minecraft.core.bukkit.event.server.ServerPayloadSendEvent;
import com.minecraft.hub.Hub;
import com.minecraft.hub.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class ServerListeners implements Listener {

    @EventHandler
    public void onBreathSent(ServerPayloadSendEvent event) {
        Lobby lobby = Hub.getInstance().getLobby();
        event.getPayload().write("id", String.valueOf(lobby.getId()));
    }

    @EventHandler
    public void onGarbageCollector(final ServerHeartbeatEvent event) {
        if (!event.isPeriodic(72000))
            return;
        Runtime.getRuntime().gc();
    }

    @EventHandler
    public void onCraft(final CraftItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(final CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM)
            event.setCancelled(true);
    }

    @EventHandler
    public void onStructureGrow(final StructureGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(final LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityCombust(final EntityCombustEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawn(final ItemSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(final WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamageEntity(final EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

}