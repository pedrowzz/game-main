package com.minecraft.bedwars.arena.island;

import com.minecraft.bedwars.arena.Arena;
import com.minecraft.bedwars.util.IslandColor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class Island {

    private final Arena arena;
    private final IslandColor color;

    private Location protectedCornerOne;
    private Location protectedCornerTwo;

    private Location upgradesEntity;
    private Location shopEntity;

    private Location bed;
    private Location spawn;

    private final Set<Player> players = new HashSet<>();

}