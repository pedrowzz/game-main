package com.minecraft.core.bukkit.arcade.game;

import lombok.Getter;

@Getter
public enum GameStyle {

    ARCADE, DUEL, PVP;

    @Getter
    private static final GameStyle[] values;

    static {
        values = values();
    }

}