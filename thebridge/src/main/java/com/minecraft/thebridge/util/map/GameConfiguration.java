package com.minecraft.thebridge.util.map;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class GameConfiguration {

    private String name;
    private Location spawnPoint, redLocation, blueLocation, blueHologram, redHologram, redCage, blueCage;

    private Set<Block> blueBlockPortals = new HashSet<>(), redBlockPortals = new HashSet<>();
    private Set<Block> invincibleBlocks = new HashSet<>();

    private int min_y, max_y;

}