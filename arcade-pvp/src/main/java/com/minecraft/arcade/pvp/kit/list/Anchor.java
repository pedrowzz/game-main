package com.minecraft.arcade.pvp.kit.list;

import com.minecraft.arcade.pvp.kit.Kit;
import com.minecraft.arcade.pvp.kit.object.KitCategory;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.bukkit.util.worldedit.Pattern;
import com.minecraft.arcade.pvp.util.EventUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Anchor extends Kit {

    protected final Vector negativeVector = new Vector(0, -1, 0);

    public Anchor() {
        setIcon(Pattern.of(Material.ANVIL));
        setKitCategory(KitCategory.COMBAT);
    }

    protected final Set<UUID> uuidSet = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnchor(EntityDamageByEntityEvent event) {
        if (EventUtils.isBothPlayers(event)) {

            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            User target = User.fetch(player.getUniqueId());
            User attacker = User.fetch(damager.getUniqueId());

            if (isUser(target) || isUser(attacker)) {
                player.setVelocity(negativeVector);
                uuidSet.add(player.getUniqueId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> player.setVelocity(negativeVector));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVelocity(PlayerVelocityEvent e) {
        if (uuidSet.contains(e.getPlayer().getUniqueId())) {
            e.setVelocity(negativeVector);
            uuidSet.remove(e.getPlayer().getUniqueId());
        }
    }

    @Override
    public void resetAttributes(User user) {
        uuidSet.remove(user.getAccount().getUniqueId());
    }

}