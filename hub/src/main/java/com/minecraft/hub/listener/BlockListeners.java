package com.minecraft.hub.listener;

import com.minecraft.hub.Hub;
import com.minecraft.hub.user.storage.UserStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListeners implements Listener {

    private final UserStorage storage;

    public BlockListeners(final Hub hub) {
        this.storage = hub.getUserStorage();
    }

    @EventHandler
    public void onPlaceBlocks(final BlockPlaceEvent event) {
        if (!storage.getUser(event.getPlayer().getUniqueId()).getAccount().getProperty("lobby.build", false).getAsBoolean())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreakBlocks(final BlockBreakEvent event) {
        if (!storage.getUser(event.getPlayer().getUniqueId()).getAccount().getProperty("lobby.build", false).getAsBoolean())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurnEvent(final BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDamageEvent(final BlockDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDispenseEvent(final BlockDispenseEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplodeEvent(final BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFormEvent(final BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromToEvent(final BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgniteEvent(final BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysicsEvent(final BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpreadEvent(final BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecayEvent(final LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockGrow(final BlockGrowEvent event) {
        event.setCancelled(true);
    }

}