package com.minecraft.thebridge.event;

import com.minecraft.core.bukkit.event.handler.ServerEvent;
import com.minecraft.thebridge.game.Game;
import com.minecraft.thebridge.team.Team;
import com.minecraft.thebridge.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLeaveGameEvent extends ServerEvent {

    private final User user;
    private final Game game;
    private final Team team;

}