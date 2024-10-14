package com.minecraft.core.bukkit.server.skywars;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameType {

    SOLO(12), DOUBLES(24);

    private final int maxPlayers;

}