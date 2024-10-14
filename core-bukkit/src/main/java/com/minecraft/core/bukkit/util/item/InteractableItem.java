package com.minecraft.core.bukkit.util.item;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class InteractableItem {

    private static final HashMap<Integer, Interact> interactables = new HashMap<>();

    private Interact interactHandler;
    private ItemStack itemStack;

    public InteractableItem(ItemStack stack, Interact handler) {
        itemStack = setNBTTag(stack, registerInteract(handler));
        interactHandler = handler;
    }

    public static int registerInteract(Interact interact) {
        if (interactables.containsValue(interact))
            return interactables.entrySet().stream().filter(entry -> entry.getValue() == interact).map(Map.Entry::getKey).findFirst().orElse(-1);
        interactables.put(interactables.size() + 1, interact);
        return interactables.size();
    }

    public static Interact getInteract(int interactId) {
        return interactables.get(interactId);
    }

    public static ItemStack setNBTTag(ItemStack stack, int id) {
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound nbtTagCompound = (nmsCopy.hasTag()) ? nmsCopy.getTag() : new NBTTagCompound();
        nbtTagCompound.set("interactable", new NBTTagInt(id));
        nmsCopy.setTag(nbtTagCompound);

        return CraftItemStack.asBukkitCopy(nmsCopy);
    }

    public enum InteractAction {
        CLICK, RIGHT, LEFT;
    }

    public enum InteractType {
        PLAYER, CLICK;
    }

    @Getter
    @Setter
    public static abstract class Interact {

        private InteractType interactType;

        public Interact(InteractType interactType) {
            this.interactType = interactType;
        }

        public Interact() {
            this.interactType = InteractType.CLICK;
        }

        public abstract boolean onInteract(Player player, Entity entity, Block block, ItemStack item, InteractAction action);
    }

}