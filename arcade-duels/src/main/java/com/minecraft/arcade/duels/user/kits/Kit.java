package com.minecraft.arcade.duels.user.kits;

import com.minecraft.core.bukkit.util.listener.DynamicListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class Kit extends DynamicListener {

    private final String name;
    private final long cooldown;
}
