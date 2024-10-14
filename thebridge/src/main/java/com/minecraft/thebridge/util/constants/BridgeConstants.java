package com.minecraft.thebridge.util.constants;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class BridgeConstants {

    public ItemStack BLUE_HELMET, BLUE_CHESTPLATE, BLUE_LEGGINGS, BLUE_BOOTS;
    public ItemStack RED_HELMET, RED_CHESTPLATE, RED_LEGGINGS, RED_BOOTS;

    public final ImmutableSet<Material> REPLACEABLE_ITEMS = Sets.immutableEnumSet(Material.STAINED_GLASS, Material.STAINED_CLAY, Material.STAINED_GLASS_PANE);

    public BridgeConstants() {
        BLUE_HELMET = new ItemStack(Material.LEATHER_HELMET);

        LeatherArmorMeta itemMeta = (LeatherArmorMeta) BLUE_HELMET.getItemMeta();
        itemMeta.setColor(Color.BLUE);
        itemMeta.spigot().setUnbreakable(true);

        BLUE_HELMET.setItemMeta(itemMeta);

        BLUE_CHESTPLATE = new ItemStack(Material.LEATHER_CHESTPLATE);

        LeatherArmorMeta itemMeta1 = (LeatherArmorMeta) BLUE_CHESTPLATE.getItemMeta();
        itemMeta1.setColor(Color.BLUE);
        itemMeta1.spigot().setUnbreakable(true);

        BLUE_CHESTPLATE.setItemMeta(itemMeta1);

        BLUE_LEGGINGS = new ItemStack(Material.LEATHER_LEGGINGS);

        LeatherArmorMeta itemMeta2 = (LeatherArmorMeta) BLUE_LEGGINGS.getItemMeta();
        itemMeta2.setColor(Color.BLUE);
        itemMeta2.spigot().setUnbreakable(true);

        BLUE_LEGGINGS.setItemMeta(itemMeta2);

        BLUE_BOOTS = new ItemStack(Material.LEATHER_BOOTS);

        LeatherArmorMeta itemMeta3 = (LeatherArmorMeta) BLUE_BOOTS.getItemMeta();
        itemMeta3.setColor(Color.BLUE);
        itemMeta3.spigot().setUnbreakable(true);

        BLUE_BOOTS.setItemMeta(itemMeta3);

        RED_HELMET = new ItemStack(Material.LEATHER_HELMET);

        LeatherArmorMeta itemMeta4 = (LeatherArmorMeta) RED_HELMET.getItemMeta();
        itemMeta4.setColor(Color.RED);
        itemMeta4.spigot().setUnbreakable(true);

        RED_HELMET.setItemMeta(itemMeta4);

        RED_CHESTPLATE = new ItemStack(Material.LEATHER_CHESTPLATE);

        LeatherArmorMeta itemMeta5 = (LeatherArmorMeta) RED_CHESTPLATE.getItemMeta();
        itemMeta5.setColor(Color.RED);
        itemMeta5.spigot().setUnbreakable(true);

        RED_CHESTPLATE.setItemMeta(itemMeta5);

        RED_LEGGINGS = new ItemStack(Material.LEATHER_LEGGINGS);

        LeatherArmorMeta itemMeta6 = (LeatherArmorMeta) RED_LEGGINGS.getItemMeta();
        itemMeta6.setColor(Color.RED);
        itemMeta6.spigot().setUnbreakable(true);

        RED_LEGGINGS.setItemMeta(itemMeta6);

        RED_BOOTS = new ItemStack(Material.LEATHER_BOOTS);

        LeatherArmorMeta itemMeta7 = (LeatherArmorMeta) RED_BOOTS.getItemMeta();
        itemMeta7.setColor(Color.RED);
        itemMeta7.spigot().setUnbreakable(true);

        RED_BOOTS.setItemMeta(itemMeta7);
    }

}