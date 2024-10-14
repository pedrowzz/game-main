package com.minecraft.pvp.kit.list;

import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.pvp.kit.Kit;
import com.minecraft.pvp.kit.KitCategory;
import com.minecraft.pvp.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Milkman extends Kit {

    public Milkman() {
        setIcon(new ItemStack(Material.MILK_BUCKET));
        setItems(new ItemFactory(Material.MILK_BUCKET).setName("§aLeite Mágico").setDescription("§7Kit Milkman").getStack());
        setCategory(KitCategory.STRATEGY);
        setCooldown(40);
        setPrice(25000);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (isItem(event.getPlayer())) {

            Player player = event.getPlayer();

            event.setCancelled(true);
            player.setItemInHand(player.getItemInHand());

            if (isCooldown(player)) {
                dispatchCooldown(player);
                return;
            }

            addCooldown(player.getUniqueId(), CooldownType.DEFAULT, 40);

            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 500, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 500, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 500, 0));
        }
    }

    @Override
    public void resetAttributes(User user) {

    }
}
