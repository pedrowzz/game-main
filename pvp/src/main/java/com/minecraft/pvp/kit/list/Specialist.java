package com.minecraft.pvp.kit.list;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.pvp.kit.Kit;
import com.minecraft.pvp.kit.KitCategory;
import com.minecraft.pvp.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Specialist extends Kit {

    private final Location enchantmentTable;
    private final ImmutableSet<Action> immutableEnumSet = Sets.immutableEnumSet(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

    public Specialist() {
        setIcon(new ItemStack(Material.BOOK));
        setCategory(KitCategory.COMBAT);
        setItems(new ItemFactory(Material.BOOK).setName("§aEncantar itens").setDescription("§7Kit Specialist").getStack());
        setPrice(30000);

        this.enchantmentTable = new Location(Bukkit.getWorld("arena"), 0.5, 125, 0.5);
        this.enchantmentTable.getBlock().setType(Material.ENCHANTMENT_TABLE);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && immutableEnumSet.contains(event.getAction()) && isUser(event.getPlayer()) && isItem(event.getPlayer())) {
            event.setCancelled(true);

            if (!enchantmentTable.getChunk().isLoaded())
                enchantmentTable.getChunk().load(true);

            event.getPlayer().openEnchanting(this.enchantmentTable, true);
        }
    }

    @Override
    public void resetAttributes(User user) {

    }

}
