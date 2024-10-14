package com.minecraft.hub.util.features.parkour;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Parkour implements Listener {

    private final Location startLocation, endLocation;
    private final List<Checkpoint> checkpoints = new ArrayList<>();

    public Parkour(final Location startLocation, final Location endLocation, Checkpoint... checkpoints) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.checkpoints.addAll(Arrays.asList(checkpoints));
    }

    protected void verifyCheckpoint(final Player player) {

    }

    protected void verifyStart(final Player player) {

    }

    @EventHandler
    public void onCheck(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;
        if (event.getMaterial() == Material.GOLD_PLATE) verifyStart(event.getPlayer());
        if (event.getMaterial() == Material.IRON_PLATE) verifyCheckpoint(event.getPlayer());
    }

}