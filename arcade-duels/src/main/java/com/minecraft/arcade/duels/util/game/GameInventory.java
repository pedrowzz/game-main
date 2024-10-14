package com.minecraft.arcade.duels.util.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GameInventory {

    private final Map<Integer, ItemStack> itemStackMap = new HashMap<>();

    private ItemStack mainItemStack;
    private ItemStack[] armor = new ItemStack[4];

    protected void addItem(int id, ItemStack itemStack) {
        itemStackMap.put(id, itemStack);
        setId(id, itemStack);
    }

    public void giveItems(final PlayerInventory inventory) {

        if (mainItemStack != null) {
            for (int i = 0; i < 36; i++)
                inventory.setItem(i, mainItemStack);
        }

        if (armor != null) {
            inventory.setArmorContents(armor);
        }

        for (Map.Entry<Integer, ItemStack> it : itemStackMap.entrySet()) {
            inventory.setItem(it.getKey(), it.getValue());
        }

    }

    public void setId(int id, ItemStack itemStack) {
        String message = "ID: " + id;
        StringBuilder builder = new StringBuilder();

        for (char c : message.toCharArray()) {
            builder.append(ChatColor.COLOR_CHAR).append(c);
        }

    }

}