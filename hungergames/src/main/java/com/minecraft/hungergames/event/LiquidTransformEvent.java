package com.minecraft.hungergames.event;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LiquidTransformEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Block block;
    private final Material transformedMaterial;

    public LiquidTransformEvent(Block block, Material transformedMaterial) {
        this.block = block;
        this.transformedMaterial = transformedMaterial;
    }

    public Block getBlock() {
        return block;
    }

    public Material getTransformedMaterial() {
        return transformedMaterial;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
