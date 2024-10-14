package com.minecraft.bedwars.arena;

import com.minecraft.bedwars.arena.island.Island;
import com.minecraft.bedwars.util.ArenaType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Arena {

    private final int id;

    private final World world;
    private final ArenaType type;

    private final Set<Island> islands = new HashSet<>();

    public int getIslandSize() {
        return this.type.getIslandSize();
    }

    public int getIslandCount() {
        return this.type.getIslandCount();
    }

    public final int getMaxPlayers() {
        return this.type.getMaxPlayers();
    }

}