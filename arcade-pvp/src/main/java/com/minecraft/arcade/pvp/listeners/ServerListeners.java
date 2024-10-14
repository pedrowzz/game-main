package com.minecraft.arcade.pvp.listeners;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.minecraft.arcade.pvp.PvP;
import com.minecraft.arcade.pvp.event.user.LivingUserDieEvent;
import com.minecraft.arcade.pvp.event.user.LivingUserInteractEvent;
import com.minecraft.arcade.pvp.game.Game;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.arcade.pvp.user.object.CombatTag;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.event.player.PlayerShowEvent;
import com.minecraft.core.bukkit.event.player.PlayerVanishDisableEvent;
import com.minecraft.core.bukkit.event.player.PlayerVanishEnableEvent;
import com.minecraft.core.bukkit.event.server.ServerPayloadSendEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.imanity.imanityspigot.movement.MovementHandler;
import org.imanity.imanityspigot.packet.wrappers.MovementPacketWrapper;

import java.util.Set;

public class ServerListeners implements Listener, MovementHandler {

    private final Set<Game> games;

    public ServerListeners() {
        Bukkit.imanity().registerMovementHandler(PvP.getInstance(), this);
        this.games = PvP.getInstance().getGameStorage().getGames();
    }

    @EventHandler
    public void onServerPayloadSendEvent(ServerPayloadSendEvent event) {
        this.games.forEach(game -> event.getPayload().write(game.getName(), game.getPlayingUsers().size()));
    }

    @EventHandler
    public void onPlayerVanishEnableEvent(PlayerVanishEnableEvent event) {
        User user = User.fetch(event.getAccount().getUniqueId());

        CombatTag combatTag = user.getCombatTag();

        if (combatTag.isTagged()) {
            new LivingUserDieEvent(user, combatTag.getLastHit(), LivingUserDieEvent.DieCause.LOGOUT, user.getInventoryContents()).fire();
        }
    }

    @EventHandler
    public void onPlayerVanishDisableEvent(final PlayerVanishDisableEvent event) {
        final User user = User.fetch(event.getAccount().getUniqueId());

        user.getGame().onPlayerJoinEvent(user, true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack() != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(event.getItemDrop().getItemStack());
            if (nmsStack != null && nmsStack.hasTag() && nmsStack.getTag().hasKey("undroppable") && nmsStack.getTag().hasKey(event.getPlayer().getUniqueId().toString()))
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItemEvent(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getType() == Material.SNOW_BALL) {
            event.setCancelled(true);
            return;
        }

        if (event.getEntity().getItemStack() != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(event.getEntity().getItemStack());
            if (nmsStack != null && nmsStack.hasTag() && nmsStack.getTag().hasKey("undroppable") && !nmsStack.getTag().hasKey("kit"))
                event.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (item != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
            if (nmsStack != null && nmsStack.hasTag() && nmsStack.getTag().hasKey("kit")) {
                if (!nmsStack.getTag().hasKey(event.getPlayer().getUniqueId().toString()))
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        if (item != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
            if (nmsStack != null && nmsStack.hasTag() && nmsStack.getTag().hasKey("kit")) {
                item.setDurability(item.getDurability());
                event.setDamage(0);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFoodLevelChangeEvent(final FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(final CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM)
            event.setCancelled(true);
    }

    @EventHandler
    public void onStructureGrow(final StructureGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(final LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityCombust(final EntityCombustEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(final WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerShowEvent(PlayerShowEvent event) {
        if (!event.getReceiver().getWorld().getUID().equals(event.getTohide().getWorld().getUID()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onProjectile(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow)
            event.getEntity().remove();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;
        if (!(event.getEntity() instanceof Player))
            return;

        final Account account = Account.fetch(event.getEntity().getUniqueId());

        if (account.hasProperty("pvp_no_damage")) {
            account.removeProperty("pvp_no_damage");

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCallInteract(final PlayerInteractEvent event) {
        final User user = User.fetch(event.getPlayer().getUniqueId());

        final LivingUserInteractEvent userInteract = new
                LivingUserInteractEvent(user, event.getItem(), event.getAction(), event.getClickedBlock(), event.getBlockFace());

        userInteract.fire();

        event.setUseInteractedBlock(userInteract.useInteractedBlock());
        event.setUseItemInHand(userInteract.useItemInHand());

        event.setCancelled(userInteract.isCancelled());
    }

    private final ImmutableSet<Material> CHECK_MATERIALS = Sets.immutableEnumSet(Material.CHEST, Material.ENCHANTMENT_TABLE, Material.ANVIL, Material.FURNACE, Material.WORKBENCH, Material.JUKEBOX, Material.ENDER_CHEST, Material.HOPPER, Material.HOPPER_MINECART, Material.DROPPER, Material.DISPENSER);

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractChest(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null)
            return;
        if (CHECK_MATERIALS.contains(block.getType())) {
            if (!block.hasMetadata("openable"))
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreakBlocks(BlockBreakEvent event) {
        if (!Account.fetch(event.getPlayer().getUniqueId()).getProperty("pvp.build", false).getAsBoolean())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlaceBlocks(BlockPlaceEvent event) {
        if (!Account.fetch(event.getPlayer().getUniqueId()).getProperty("pvp.build", false).getAsBoolean())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!Account.fetch(event.getPlayer().getUniqueId()).getProperty("pvp.build", false).getAsBoolean())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (!Account.fetch(event.getPlayer().getUniqueId()).getProperty("pvp.build", false).getAsBoolean())
            event.setCancelled(true);
    }

    protected void verifyBlocks(final Player player) {
        switch (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()) {
            case SPONGE: {
                final Vector vector = player.getVelocity();

                vector.setX(0);
                vector.setY(0.35);
                vector.setZ(0);

                player.setVelocity(vector);

                if (player.getLocation().getBlock().getType() == Material.AIR) {
                    final Account account = Account.fetch(player.getUniqueId());

                    if (!account.hasProperty("pvp_no_damage"))
                        account.setProperty("pvp_no_damage", true);
                }
            }

            case EMERALD_BLOCK: {
                player.setVelocity(player.getVelocity().multiply(4.5).setY(-0.6));
            }
        }
    }

    @Override
    public void onUpdateLocation(Player player, Location location, Location location1, MovementPacketWrapper movementPacketWrapper) {
        this.verifyBlocks(player);
    }

    @Override
    public void onUpdateRotation(Player player, Location location, Location location1, MovementPacketWrapper movementPacketWrapper) {
        this.verifyBlocks(player);
    }

}