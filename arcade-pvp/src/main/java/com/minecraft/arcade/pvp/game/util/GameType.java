package com.minecraft.arcade.pvp.game.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum GameType {

    SINGLEKIT(1, "Single Kit"),
    DOUBLEKIT(2, "Double Kit"),
    TRIPLEKIT(3, "Triple Kit"),
    QUADRUPLEKIT(4, "Quadruple Kit");

    private final int maxKits;
    private final String name;

    public static GameType fromString(String string) {
        return Arrays.stream(values()).filter(prefixType -> prefixType.name().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

}