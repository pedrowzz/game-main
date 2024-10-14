package com.minecraft.arcade.pvp.event.user;

import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.bukkit.event.handler.ServerEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LivingUserLostProtectionEvent extends ServerEvent {

    private final User user;

}