package com.minecraft.core.bukkit.arcade.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum GameQuantity {

    NONE("Nenhum", 0, 0),
    SOLO("Solo", 1, 8),
    DOUBLES("Duplas", 2, 8),
    THREESOME("Trios", 3, 4),
    GROUP("Quartetos", 4, 4);

    private final String name;
    private final int playerCount, teamCount;

    public int getMaxPlayers(int teamSize) {
        return this.playerCount * teamSize;
    }

    public int getMaxPlayers() {
        return getMaxPlayers(this.teamCount);
    }

    @Getter
    private static final GameQuantity[] values;

    static {
        values = values();
    }

    public static GameQuantity fetch(String s) {
        return Arrays.stream(getValues()).filter(arg -> arg.name().equalsIgnoreCase(s)).findFirst().orElse(null);
    }
}