package com.minecraft.core.bukkit.arcade.route;

import com.minecraft.core.bukkit.arcade.game.GameQuantity;
import com.minecraft.core.bukkit.arcade.game.GameType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class GameRouteContext {

    private final GameType type;
    private final GameQuantity quantity;

    private final int map, slots;

    private final JoinMode mode;

    private final List<UUID> link;

}