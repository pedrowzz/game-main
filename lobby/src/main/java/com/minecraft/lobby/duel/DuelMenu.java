package com.minecraft.lobby.duel;

import com.minecraft.core.bukkit.util.inventory.Selector;
import com.minecraft.core.bukkit.util.item.InteractableItem;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.lobby.command.DuelCommand;
import com.minecraft.lobby.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class DuelMenu {

    private final User sender, target;

    public void build() {

        Selector.Builder builder = new Selector.Builder();
        List<ItemStack> itemStacks = new ArrayList<>();

        for (DuelCommand.DuelIcons duelIcon : DuelCommand.DuelIcons.values()) {
            boolean alreadyInvited = target.alreadyInvited(sender.getUniqueId(), duelIcon.getDuelType());

            ItemFactory itemFactory = duelIcon.getItemStack();
            itemFactory.setDescription(alreadyInvited ? "§eClique para aceitar o convite!" : "§eClique para desafiar!");

            itemFactory.addItemFlag(ItemFlag.values());

            InteractableItem.Interact interact = new InteractableItem.Interact() {
                @Override
                public boolean onInteract(Player player, Entity entity, Block block, ItemStack item, InteractableItem.InteractAction action) {
                    player.closeInventory();
                    DuelCommand.duel(sender, target, duelIcon.getDuelType());
                    return true;
                }
            };

            itemStacks.add(new InteractableItem(duelIcon.getItemStack().getStack(), interact).getItemStack());
        }

        builder.withSize(27);
        builder.withItems(itemStacks);
        builder.withPreviousPageSlot(18);
        builder.withNextPageSlot(26);
        builder.withName("Duel a player");
        builder.build().open(sender.getPlayer());
    }
}
