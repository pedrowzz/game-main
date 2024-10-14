package com.minecraft.thebridge.event;

import com.minecraft.core.bukkit.event.handler.ServerEvent;
import com.minecraft.thebridge.game.Game;
import com.minecraft.thebridge.team.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GameEndEvent extends ServerEvent {

    private final Game game;
    private final Reason reason;

    @Setter
    private Team winner, loser;

    public enum Reason {
        LOGOUT, POINTS, TIME;
    }

}