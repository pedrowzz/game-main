package com.minecraft.core.bukkit.arcade.room;

import lombok.Getter;

@Getter
public enum RoomStage {

    WAITING, STARTING, PLAYING, ENDING, RESETTING;

    @Getter
    private static final RoomStage[] values;

    static {
        values = values();
    }

}