package com.minecraft.core.bukkit.util.selector.opener;

import com.google.common.base.Preconditions;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.util.selector.InventoryService;
import com.minecraft.core.bukkit.util.selector.SmartInventory;
import com.minecraft.core.translation.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ChestInventoryOpener implements InventoryOpener {

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        return open(inv, player, inv.getTitle());
    }


    @Override
    public Inventory open(SmartInventory inv, Player player, String title) {
        Preconditions.checkArgument(inv.getColumns() == 9,
                "The column count for the chest inventory must be 9, found: %s.", inv.getColumns());
        Preconditions.checkArgument(inv.getRows() >= 1 && inv.getRows() <= 6,
                "The row count for the chest inventory must be between 1 and 6, found: %s", inv.getRows());

        if (title == null) {
            return open(inv, player);
        }

        Account account = Account.fetch(player.getUniqueId());
        Language language = account.getLanguage();

        InventoryService manager = inv.getManager();
        Inventory handle = Bukkit.createInventory(player, inv.getRows() * inv.getColumns(), language.translate(inv.getTitle()));

        fill(handle, manager.getContents(player).get());

        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST;
    }

}
