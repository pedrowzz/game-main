package com.minecraft.thebridge.event;

import com.minecraft.core.bukkit.event.handler.ServerEvent;
import com.minecraft.thebridge.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameStartEvent extends ServerEvent {

    private final Game game;

}