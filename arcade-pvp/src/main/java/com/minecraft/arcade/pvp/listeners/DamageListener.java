/*
 * Copyright (C) YoloMC, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

package com.minecraft.arcade.pvp.listeners;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.minecraft.arcade.pvp.util.EventUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageListener implements Listener {

    private final ImmutableSet<Material> ACCEPTABLE_MATERIALS = Sets.immutableEnumSet(Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE, Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE);

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (EventUtils.isBothPlayers(event)) {
            Player player = (Player) event.getDamager();
            ItemStack itemStack = player.getItemInHand();
            if (itemStack != null && ACCEPTABLE_MATERIALS.contains(itemStack.getType()))
                event.setDamage(Math.max(2, event.getDamage() - 2.00));
            else if (itemStack != null && itemStack.getType() == Material.MUSHROOM_SOUP)
                event.setDamage(1);
        }
    }

}