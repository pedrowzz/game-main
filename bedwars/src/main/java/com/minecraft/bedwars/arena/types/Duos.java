package com.minecraft.bedwars.arena.types;

import com.minecraft.bedwars.arena.Arena;
import com.minecraft.bedwars.util.ArenaType;
import org.bukkit.World;

public class Duos extends Arena {

    public Duos(int id, World world) {
        super(id, world, ArenaType.DUOS);
    }

}