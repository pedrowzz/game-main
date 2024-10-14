package com.minecraft.arcade.pvp.listeners;

import com.minecraft.arcade.pvp.event.user.LivingUserDieEvent;
import com.minecraft.arcade.pvp.kit.Kit;
import com.minecraft.arcade.pvp.kit.object.CooldownType;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.arcade.pvp.user.object.CombatTag;
import com.minecraft.arcade.pvp.util.Assistance;
import com.minecraft.core.bukkit.util.BukkitInterface;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathListeners implements Listener, Assistance, BukkitInterface {

    protected final int COMBAT_DURATION = 15;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        User user = getUser(event.getEntity().getUniqueId());

        user.getPlayer().setHealth(20);

        event.getDrops().removeIf(drop -> drop == null || drop.getType() == Material.AIR || hasKey(drop, "undroppable"));

        CombatTag combatTag = user.getCombatTag();

        User killer = (event.getEntity().getKiller() != null ? getUser(event.getEntity().getKiller().getUniqueId()) : combatTag.isTagged() ? combatTag.getLastHit() : null);

        new LivingUserDieEvent(user, killer, LivingUserDieEvent.DieCause.KILL, event.getDrops()).fire();

        event.getDrops().clear();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        User user = getUser(event.getPlayer().getUniqueId());

        CombatTag combatTag = user.getCombatTag();

        if (combatTag.isTagged()) {
            List<ItemStack> drops = new ArrayList<>(user.getInventoryContents());

            drops.removeIf(drop -> drop == null || drop.getType() == Material.AIR || hasKey(drop, "undroppable"));

            new LivingUserDieEvent(user, combatTag.getLastHit(), LivingUserDieEvent.DieCause.LOGOUT, drops).fire();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVoidDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (event.getCause() != EntityDamageEvent.DamageCause.VOID)
            return;

        User user = getUser(event.getEntity().getUniqueId());

        CombatTag combatTag = user.getCombatTag();

        if (combatTag.isTagged()) {
            List<ItemStack> drops = new ArrayList<>(user.getInventoryContents());

            drops.removeIf(drop -> drop == null || drop.getType() == Material.AIR || hasKey(drop, "undroppable"));

            new LivingUserDieEvent(user, combatTag.getLastHit(), LivingUserDieEvent.DieCause.KILL, drops).fire();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            UUID uuid = event.getEntity().getUniqueId();

            final User user = getUser(uuid);
            final User damager = getUser(event.getDamager().getUniqueId());

            for (final Kit kit : user.getKits()) {
                if (kit.isCombatCooldown()) {
                    kit.addCooldown(uuid, CooldownType.COMBAT, kit.getCombatCooldown());
                }
            }

            user.getCombatTag().addTag(damager, COMBAT_DURATION);
        } else if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {

            final Entity entity = event.getEntity();
            final Projectile projectile = (Projectile) event.getDamager();

            if (projectile.getShooter() instanceof Player) {

                final Player shooter = (Player) projectile.getShooter();

                if (entity.getEntityId() != shooter.getEntityId()) {

                    final UUID uuid = event.getEntity().getUniqueId();
                    final User user = getUser(uuid);

                    for (final Kit kit : user.getKits()) {
                        if (kit.isCombatCooldown()) {
                            kit.addCooldown(uuid, CooldownType.COMBAT, kit.getCombatCooldown());
                        }
                    }

                    user.getCombatTag().addTag(getUser(shooter.getUniqueId()), COMBAT_DURATION);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSplashPotion(final PotionSplashEvent event) {
        final ThrownPotion thrownPotion = event.getPotion();

        if (!(thrownPotion.getShooter() instanceof Player))
            return;

        final Player attacker = (Player) thrownPotion.getShooter();

        event.getAffectedEntities().forEach(livingEntity -> {
            if (!(livingEntity instanceof Player)) return;

            if (livingEntity.getEntityId() == attacker.getEntityId()) return;

            final User user = getUser(livingEntity.getUniqueId());

            user.getCombatTag().addTag(getUser(attacker.getUniqueId()), COMBAT_DURATION);
        });
    }

}