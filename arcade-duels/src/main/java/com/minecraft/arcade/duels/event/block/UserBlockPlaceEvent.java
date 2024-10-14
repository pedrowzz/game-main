package com.minecraft.arcade.duels.event.block;

import com.minecraft.arcade.duels.user.User;
import com.minecraft.core.bukkit.event.handler.ServerEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
@Setter
public class UserBlockPlaceEvent extends ServerEvent implements Cancellable {

    private final User user;
    private final Block block, blockPlaced;
    private final ItemStack itemStack;
    private boolean cancelled;

}
