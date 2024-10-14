package com.minecraft.auth.util;

import com.minecraft.auth.Auth;
import com.minecraft.core.bukkit.util.item.InteractableItem;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Captcha {

    @Getter
    private Inventory inventory;

    public static Captcha newInstance() {
        Captcha captcha = new Captcha();

        captcha.inventory = Bukkit.createInventory(null, 27, "Clique no bloco verde");

        for (int i = 0; i < 27; i++) {
            captcha.inventory.setItem(i, wrong);
        }

        captcha.inventory.setItem(nextInt(0, 26), correct);
        return captcha;
    }

    private static int nextInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    private static final ItemStack wrong = new InteractableItem(new ItemFactory(Material.STAINED_CLAY).setDurability(9).setName(" ").getStack(), new InteractableItem.Interact() {
        @Override
        public boolean onInteract(Player player, Entity entity, Block block, ItemStack item, InteractableItem.InteractAction action) {
            player.removeMetadata("captcha_challenge", Auth.getAuth());
            player.kickPlayer("§cVocÊ falhou o desafio, tente novamente.");
            return true;
        }
    }).getItemStack();

    private static final ItemStack correct = new InteractableItem(new ItemFactory(Material.STAINED_CLAY).setDurability(5).setName(" ").getStack(), new InteractableItem.Interact() {
        @Override
        public boolean onInteract(Player player, Entity entity, Block block, ItemStack item, InteractableItem.InteractAction action) {

            player.removeMetadata("captcha_challenge", Auth.getAuth());
            player.closeInventory();

            try {
                String message = "captcha_successful";

                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);

                out.writeUTF(message);
                player.sendPluginMessage(Auth.getAuth(), "Auth", b.toByteArray());
                b.close();
                out.close();

            } catch (Exception e) {
                player.kickPlayer("unexpected_error");
                e.printStackTrace();
            }

            return true;
        }
    }).getItemStack();

}
