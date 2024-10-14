package com.minecraft.bedwars.arena.types;

import com.minecraft.bedwars.arena.Arena;
import com.minecraft.bedwars.util.ArenaType;
import org.bukkit.World;

public class Solo extends Arena {

    public Solo(int id, World world) {
        super(id, world, ArenaType.SOLO);
    }

}