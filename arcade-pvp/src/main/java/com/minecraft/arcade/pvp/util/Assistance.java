package com.minecraft.arcade.pvp.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.minecraft.arcade.pvp.PvP;
import com.minecraft.arcade.pvp.user.User;
import net.minecraft.server.v1_8_R3.NBTTagByte;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface Assistance {

    ImmutableSet<Action> LEFT_INTERACT = Sets.immutableEnumSet(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK);

    ImmutableSet<Action> RIGHT_INTERACT = Sets.immutableEnumSet(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

    default ItemStack addTag(ItemStack stack, String... tag) {
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound nbtTagCompound = (nmsCopy.hasTag()) ? nmsCopy.getTag() : new NBTTagCompound();
        for (String str : tag)
            nbtTagCompound.set(str, new NBTTagByte((byte) 0));
        nmsCopy.setTag(nbtTagCompound);
        return CraftItemStack.asBukkitCopy(nmsCopy);
    }

    default boolean hasKey(ItemStack itemStack, String key) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsStack != null && nmsStack.hasTag() && nmsStack.getTag().hasKey(key);
    }

    default PvP getInstance() {
        return PvP.getInstance();
    }

    default User getUser(UUID uuid) {
        return User.fetch(uuid);
    }

}