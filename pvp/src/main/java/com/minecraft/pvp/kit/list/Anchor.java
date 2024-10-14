package com.minecraft.pvp.kit.list;

import com.minecraft.pvp.kit.Kit;
import com.minecraft.pvp.kit.KitCategory;
import com.minecraft.pvp.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Anchor extends Kit {

    protected final Set<UUID> uuidSet = new HashSet<>();

    public Anchor() {
        setIcon(new ItemStack(Material.ANVIL));
        setCategory(KitCategory.COMBAT);
        setPrice(35000);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVelocity(PlayerVelocityEvent e) {
        if (uuidSet.contains(e.getPlayer().getUniqueId())) {
            e.setVelocity(new Vector(0, -1, 0));
            uuidSet.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnchor(EntityDamageByEntityEvent event) {
        if (event.isBothPlayers()) {

            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            User target = User.fetch(player.getUniqueId());
            User attacker = User.fetch(damager.getUniqueId());

            Kit neo = getPlugin().getKitStorage().getKit("Neo");

            if (target.getKit1().getName().equals(neo.getName()) || target.getKit2().getName().equals(neo.getName()) || attacker.getKit1().getName().equals(neo.getName()) || attacker.getKit2().getName().equals(neo.getName()))
                return;

            if (isUser(target) || isUser(damager)) {
                player.setVelocity(new Vector(0, -1, 0));
                uuidSet.add(player.getUniqueId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> player.setVelocity(new Vector(0, -1, 0)));
            }
        }
    }

    @Override
    public void resetAttributes(User user) {
        uuidSet.remove(user.getUniqueId());
    }

}
