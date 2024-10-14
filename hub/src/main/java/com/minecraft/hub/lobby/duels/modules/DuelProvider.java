package com.minecraft.hub.lobby.duels.modules;

import com.minecraft.hub.Hub;
import com.minecraft.hub.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DuelProvider implements Listener {

    public DuelProvider(final Hub hub) {
        hub.getServer().getPluginManager().registerEvents(this, hub);
    }

    public void openDuelChoose(final User challenger, final User challenged) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent event) {
        final Entity entity = event.getRightClicked();

        if (entity instanceof Player) {
            final Player player = event.getPlayer();
            final ItemStack stack = player.getItemInHand();

            if (stack == null || stack.getType() != Material.BLAZE_ROD)
                return;

            openDuelChoose(User.fetch(player.getUniqueId()), User.fetch(entity.getUniqueId()));

            event.setCancelled(true);
        }
    }

}