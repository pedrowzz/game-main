package com.minecraft.thebridge.event;

import com.minecraft.core.bukkit.event.handler.ServerEvent;
import com.minecraft.core.bukkit.server.route.PlayMode;
import com.minecraft.thebridge.game.Game;
import com.minecraft.thebridge.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinGameEvent extends ServerEvent {

    private final User user;
    private final Game game;
    private final PlayMode mode;
    private final boolean teleport;

}