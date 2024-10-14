package com.minecraft.arcade.pvp.event.user;

import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.bukkit.event.handler.ServerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
public class LivingUserInteractEvent extends ServerEvent implements Cancellable {

    private final User user;

    private ItemStack item;
    private Action action;
    private Block blockClicked;
    private BlockFace blockFace;

    private Result useClickedBlock;
    private Result useItemInHand;

    public LivingUserInteractEvent(final User user, final ItemStack itemStack, final Action action, final Block blockClicked, final BlockFace blockFace) {
        this.user = user;
        this.item = itemStack;
        this.action = action;
        this.blockClicked = blockClicked;
        this.blockFace = blockFace;
        this.useItemInHand = Result.DEFAULT;
        this.useClickedBlock = blockClicked == null ? Result.DENY : Result.ALLOW;
    }

    public boolean isCancelled() {
        return this.useInteractedBlock() == Result.DENY;
    }

    public void setCancelled(boolean cancel) {
        this.setUseInteractedBlock(cancel ? Result.DENY : (this.useInteractedBlock() == Result.DENY ? Result.DEFAULT : this.useInteractedBlock()));
        this.setUseItemInHand(cancel ? Result.DENY : (this.useItemInHand() == Result.DENY ? Result.DEFAULT : this.useItemInHand()));
    }

    public Result useInteractedBlock() {
        return this.useClickedBlock;
    }

    public void setUseInteractedBlock(Result useInteractedBlock) {
        this.useClickedBlock = useInteractedBlock;
    }

    public Result useItemInHand() {
        return this.useItemInHand;
    }

    public void setUseItemInHand(Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

}