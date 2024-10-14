package com.minecraft.duels.listener;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.event.server.ServerHeartbeatEvent;
import com.minecraft.core.bukkit.server.route.PlayMode;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.vanish.Vanish;
import com.minecraft.duels.Duels;
import com.minecraft.duels.event.player.UserDeathEvent;
import com.minecraft.duels.map.config.MapConfiguration;
import com.minecraft.duels.mode.Mode;
import com.minecraft.duels.room.Room;
import com.minecraft.duels.room.team.Team;
import com.minecraft.duels.user.User;
import com.minecraft.duels.util.FireworkAPI;
import com.minecraft.duels.util.enums.RoomStage;
import com.minecraft.duels.util.visibility.Visibility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ServerListener implements Listener, BukkitInterface {

    private final Duels instance;

    public ServerListener(Duels duels) {
        this.instance = duels;
    }

    @EventHandler
    public void onDeath(UserDeathEvent event) {
        User user = event.getUser();
        Player player = user.getPlayer();

        player.closeInventory();

        Room room = user.getRoom();
        Mode mode = room.getMode();

        Team team = room.getBlue().getMembers().contains(user) ? room.getBlue() : room.getRed();
        team.getMembers().remove(user);

        Account account = user.getAccount();

        if (!event.isDefinitelyLeft()) {
            room.getSpectators().add(user);
            mode.join(user, PlayMode.VANISH);

            room.getWorld().getPlayers().forEach(players -> players.sendMessage(team.getChatColor() + user.getName() + " §emorreu."));

            for (int i = 0; i < 4; i++)
                FireworkAPI.random(player.getLocation());

            if (room.isCountStats()) {
                account.addInt(1, mode.getLoses());
                account.getData(mode.getWinstreak()).setData(0);
            }
        }

        Visibility.refresh(player);

        if (room.isCountStats()) {
            async(() -> {
                if (room.getStage() == RoomStage.PLAYING || room.getStage() == RoomStage.ENDING) {
                    for (User other : room.getAlivePlayers()) {
                        float winnerRating = other.getAccount().getData(mode.getRating()).getAsInt();
                        float loserRating = account.getData(mode.getRating()).getAsInt();

                        float Pb = 1.0f / (1 + (float) (Math.pow(10, (winnerRating - loserRating) / 400)));
                        float Pa = 1.0f / (1 + (float) (Math.pow(10, (loserRating - winnerRating) / 400)));

                        int newWinnerRating = (int) Math.max(winnerRating + 3, Math.round(winnerRating + 32 * (1 - Pa)));
                        int newLoserRating = Math.round(loserRating + 32 * (0 - Pb));

                        other.getAccount().getData(mode.getRating()).setData(newWinnerRating);
                        account.getData(mode.getRating()).setData(newLoserRating);

                        other.getPlayer().sendMessage("§a+" + ((int) (newWinnerRating - winnerRating)) + " Rating");
                        player.sendMessage("§c" + ((int) (newLoserRating - loserRating)) + " Rating");
                    }
                }
                user.getAccount().getDataStorage().saveTable(user.getRouteContext().getGame().getTable());
            });
        }
    }

    private final ImmutableSet<Material> CHECK_MATERIALS = Sets.immutableEnumSet(Material.CHEST, Material.ENCHANTMENT_TABLE, Material.ANVIL, Material.FURNACE, Material.JUKEBOX, Material.ENDER_CHEST, Material.HOPPER, Material.HOPPER_MINECART, Material.DROPPER, Material.DISPENSER);

    @EventHandler
    public void onInteractChest(PlayerInteractEvent event) {
        if (!event.hasBlock())
            return;

        Block block = event.getClickedBlock();
        World world = block.getWorld();
        Room room = instance.getRoomStorage().getRoom(world);

        if (room == null || room.getRollback().contains(block)) {
            return;
        }

        if (CHECK_MATERIALS.contains(block.getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStopDeath(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.isCancelled())
                return;

            Player p = (Player) event.getEntity();

            if (p.getHealth() - event.getFinalDamage() <= 0) {
                event.setCancelled(true);
                p.setHealth(20.0D);

                User user = User.fetch(p.getUniqueId());
                new UserDeathEvent(user, false).fire();
            }
        }
    }

    @EventHandler
    public void ifDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getDrops().clear();
    }

    @EventHandler
    public void onHeartbeat(ServerHeartbeatEvent event) {
        if (event.isPeriodic(20)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                User user = User.fetch(player.getUniqueId());

                if (user == null)
                    continue;

                if (Vanish.getInstance().isVanished(player.getUniqueId()))
                    continue;

                Room room = user.getRoom();

                if (room == null)
                    continue;

                MapConfiguration mapConfiguration = user.getRoom().getMapConfiguration();

                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();

                if (absolute(x) >= mapConfiguration.getSize() || y >= mapConfiguration.getHeight() || absolute(z) >= mapConfiguration.getSize()) {
                    player.teleport(mapConfiguration.getSpawnPoint());
                }
            }
        }
    }

}