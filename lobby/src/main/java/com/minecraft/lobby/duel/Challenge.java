package com.minecraft.lobby.duel;

import com.minecraft.core.bukkit.server.duels.DuelType;
import com.minecraft.lobby.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Challenge {

    private final User sender, receiver;
    private final DuelType duelType;
    private final long created = System.currentTimeMillis();

    public boolean expired() {
        return created + 60000 < System.currentTimeMillis();
    }
}
