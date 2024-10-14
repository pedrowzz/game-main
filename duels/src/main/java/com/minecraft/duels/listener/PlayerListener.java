package com.minecraft.duels.listener;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.event.player.PlayerShowEvent;
import com.minecraft.core.bukkit.event.player.PlayerTeamAssignEvent;
import com.minecraft.core.bukkit.event.player.PlayerVanishDisableEvent;
import com.minecraft.core.bukkit.event.player.PlayerVanishEnableEvent;
import com.minecraft.core.bukkit.event.protocol.PacketReceiveEvent;
import com.minecraft.core.bukkit.server.route.PlayMode;
import com.minecraft.core.bukkit.util.cooldown.CooldownProvider;
import com.minecraft.core.bukkit.util.cooldown.type.Cooldown;
import com.minecraft.core.bukkit.util.vanish.Vanish;
import com.minecraft.duels.Duels;
import com.minecraft.duels.room.Room;
import com.minecraft.duels.user.User;
import com.minecraft.duels.util.enums.RoomStage;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {

    private final Duels instance;

    public PlayerListener(Duels duels) {
        this.instance = duels;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(final PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onGoldenInteract(PlayerInteractEvent event) {
        if (!event.hasItem())
            return;

        if (event.getItem().getType() != Material.SKULL_ITEM)
            return;

        if (!event.getAction().name().contains("RIGHT"))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        Cooldown cooldown = CooldownProvider.getGenericInstance().getCooldown(player.getUniqueId(), "golden_head");

        if (cooldown != null) {
            player.sendMessage(Account.fetch(player.getUniqueId()).getLanguage().translate("wait_generic", Constants.SIMPLE_DECIMAL_FORMAT.format(cooldown.getRemaining())));
            return;
        }

        int amount = event.getItem().getAmount();

        if (amount <= 1)
            event.getPlayer().setItemInHand(null);

        event.getItem().setAmount(amount - 1);

        PotionEffectType ABSORPTION = PotionEffectType.ABSORPTION;

        if (player.hasPotionEffect(ABSORPTION))
            player.removePotionEffect(ABSORPTION);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0));
        player.addPotionEffect(new PotionEffect(ABSORPTION, 2400, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));

        player.updateInventory();
        player.playSound(event.getPlayer().getLocation(), Sound.HURT_FLESH, 1, 1);

        CooldownProvider.getGenericInstance().addCooldown(player.getUniqueId(), "golden_head", "golden_head", 2.5, false);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            User user = User.fetch(player.getUniqueId());

            Room room = user.getRoom();

            if (room == null || room.getStage() != RoomStage.PLAYING || !user.isPlaying())
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isBothPlayers()) {
            Player player = (Player) event.getEntity();
            User user = User.fetch(player.getUniqueId());

            Room room = user.getRoom();

            if (room == null || room.getStage() != RoomStage.PLAYING || !user.isPlaying() && !Vanish.getInstance().isVanished(player.getUniqueId()))
                event.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        Room room = user.getRoom();

        if (room == null || room.getStage() != RoomStage.PLAYING || !user.isPlaying())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        Room room = user.getRoom();

        if (room == null || room.getStage() != RoomStage.PLAYING || !user.isPlaying())
            event.setCancelled(true);
    }

    @EventHandler
    private void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAwardAchievement(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onVanishEnable(PlayerVanishEnableEvent event) {

        Player player = Bukkit.getPlayer(event.getAccount().getUniqueId());
        User user = User.fetch(event.getAccount().getUniqueId());
        Room room = user.getRoom();

        if (room != null) {
            if (!room.isSpectator(user)) {
                player.sendMessage("§aReentrando como espectador.");
                room.join(user, PlayMode.VANISH, true);
            }
        }
    }

    @EventHandler
    public void onVanishEnable(PlayerVanishDisableEvent event) {

        Player player = Bukkit.getPlayer(event.getAccount().getUniqueId());
        User user = User.fetch(event.getAccount().getUniqueId());
        Room room = user.getRoom();

        if (room != null) {
            if (room.isSpectator(user)) {
                player.sendMessage("§aReentrando como jogador.");
                room.join(user, PlayMode.PLAYER, true);
            }
        }
    }

    @EventHandler
    public void onShow(PlayerShowEvent event) {
        if (!event.getReceiver().getWorld().getUID().equals(event.getTohide().getWorld().getUID()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());
        Room room = user.getRoom();

        if (room == null || room.getStage() == RoomStage.WAITING || room.getStage() == RoomStage.STARTING) {
            player.sendMessage(user.getAccount().getLanguage().translate("duels.cant_pregame_chat"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof PacketPlayInTabComplete) {
            Player player = event.getPlayer();
            User user = User.fetch(player.getUniqueId());
            Room room = user.getRoom();

            if (room == null || room.getStage() == RoomStage.WAITING || room.getStage() == RoomStage.STARTING)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeamAssign(PlayerTeamAssignEvent event) {
        Account account = event.getAccount();
        User user = User.fetch(account.getUniqueId());

        if (user.isPlaying()) {

            Room room = user.getRoom();

            if (room == null || room.getStage() == RoomStage.WAITING || room.getStage() == RoomStage.STARTING) {
                event.getTeam().setPrefix("§7§k");
            } else if (room.getBlue().getMembers().contains(user)) {
                event.getTeam().setPrefix("§9");
            } else {
                event.getTeam().setPrefix("§c");
            }
        }
    }
}