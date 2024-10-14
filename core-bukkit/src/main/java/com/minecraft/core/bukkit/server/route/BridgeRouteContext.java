package com.minecraft.core.bukkit.server.route;

import com.minecraft.core.bukkit.server.thebridge.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BridgeRouteContext {

    private UUID target;
    private PlayMode playMode;
    private GameType gameType;

    private boolean hasTarget() {
        return target != null;
    }

}