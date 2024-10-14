package com.minecraft.core.bukkit.listener;

import com.minecraft.core.bukkit.event.player.PlayerSoupDrinkEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class SoupListener implements Listener {

    public SoupListener(final Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(final PlayerInteractEvent event) {  /* Soup Listener */
        if (!event.getAction().name().contains("RIGHT")) return;

        if (event.getItem() == null) return;

        if (event.getItem().getType() != Material.MUSHROOM_SOUP) return;

        event.setCancelled(true);

        final Player player = event.getPlayer();

        double beforeHealth = player.getHealth();

        if (beforeHealth < player.getMaxHealth()) {
            if ((beforeHealth + 7) > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
                if (player.getFoodLevel() < 20) {
                    double i = (beforeHealth + 7) - player.getMaxHealth();
                    player.setFoodLevel((Math.min(player.getFoodLevel() + (int) i, 20)));
                    player.setSaturation(3);
                }
            } else {
                player.setHealth(player.getHealth() + 7);
            }

            PlayerSoupDrinkEvent soupDrinkEvent = new PlayerSoupDrinkEvent(player, new ItemStack(Material.BOWL));
            soupDrinkEvent.fire();

            player.setItemInHand(soupDrinkEvent.getItemStack());
        } else if (player.getFoodLevel() < 20) {
            player.setFoodLevel(player.getFoodLevel() + 7);
            player.setSaturation(3);

            PlayerSoupDrinkEvent soupDrinkEvent = new PlayerSoupDrinkEvent(player, new ItemStack(Material.BOWL));
            soupDrinkEvent.fire();

            player.setItemInHand(soupDrinkEvent.getItemStack());
        }
    }

}