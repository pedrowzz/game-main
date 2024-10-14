package com.minecraft.thebridge.event;

import com.minecraft.core.bukkit.event.handler.ServerEvent;
import com.minecraft.thebridge.game.Game;
import com.minecraft.thebridge.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDiedEvent extends ServerEvent {

    private final User killed, killer;
    private final Game game;

    public boolean hasKiller() {
        return killer != null;
    }

}