package com.minecraft.skywars.game.types;

import com.minecraft.core.bukkit.server.skywars.GameType;
import com.minecraft.skywars.game.Game;

public class Solo extends Game {

    public Solo(int id) {
        super(id, GameType.SOLO);
    }

}