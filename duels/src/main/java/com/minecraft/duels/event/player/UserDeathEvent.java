package com.minecraft.duels.event.player;

import com.minecraft.core.bukkit.event.handler.ServerEvent;
import com.minecraft.duels.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeathEvent extends ServerEvent {

    private User user;
    private boolean definitelyLeft;

}
