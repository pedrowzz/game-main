package com.minecraft.arcade.duels.event.block;

import com.minecraft.arcade.duels.user.User;
import com.minecraft.core.bukkit.event.handler.ServerEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

@Getter
@Setter
@RequiredArgsConstructor
public class UserBlockBreakEvent extends ServerEvent implements Cancellable {

    private final User user;
    private final Block block;
    private boolean dropItems = true;
    private boolean cancelled = false;
}
