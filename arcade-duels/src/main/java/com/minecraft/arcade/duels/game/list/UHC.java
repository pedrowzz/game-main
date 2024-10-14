package com.minecraft.arcade.duels.game.list;

import com.minecraft.arcade.duels.Duels;
import com.minecraft.arcade.duels.game.Game;
import com.minecraft.core.bukkit.arcade.game.GameType;

public class UHC extends Game {

    public UHC(Duels plugin, Integer minRooms, Integer maxRooms, String mapDirectory) {
        super(plugin, minRooms, maxRooms, GameType.UHC, mapDirectory);
    }

    @Override
    public boolean isCanBuild(boolean blockMap) {
        return true;
    }
}