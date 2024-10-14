package com.minecraft.bedwars.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArenaType {

    SOLO(1, 8),
    DUOS(2, 8),
    TRIOS(3, 4),
    QUARTETS(4, 4);

    private final int islandSize, islandCount;

    public final int getMaxPlayers() {
        return this.islandSize * this.islandCount;
    }

}