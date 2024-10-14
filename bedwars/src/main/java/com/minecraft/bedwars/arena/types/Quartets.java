package com.minecraft.bedwars.arena.types;

import com.minecraft.bedwars.arena.Arena;
import com.minecraft.bedwars.util.ArenaType;
import org.bukkit.World;

public class Quartets extends Arena {

    public Quartets(int id, World world) {
        super(id, world, ArenaType.QUARTETS);
    }

}