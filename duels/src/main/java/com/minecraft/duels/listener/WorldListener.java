package com.minecraft.duels.listener;

import com.minecraft.core.bukkit.util.vanish.Vanish;
import com.minecraft.duels.Duels;
import com.minecraft.duels.room.Room;
import com.minecraft.duels.user.User;
import com.minecraft.duels.util.enums.RoomStage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Set;

public class WorldListener implements Listener {

    private final Duels instance;

    public WorldListener(Duels duels) {
        this.instance = duels;
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        if (!user.isPlaying()) {
            event.setCancelled(true);
        } else if (!user.getRoom().getMode().isAllowDrops()) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntityType() != EntityType.PLAYER && event.getEntityType() != EntityType.DROPPED_ITEM)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMapBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        Room room = user.getRoom();

        if (room == null || room.getStage() != RoomStage.PLAYING) {
            event.setCancelled(true);
            return;
        }

        if (!room.getMode().isCanBuild()) {
            event.setCancelled(true);
            return;
        }

        if (room.isOutside(player.getLocation()) || room.isOutside(event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (!user.isPlaying() && !Vanish.getInstance().isVanished(user.getUniqueId())) {
            event.setCancelled(true);
        } else if (!room.getRollback().contains(event.getBlock())) {
            player.sendMessage(user.getAccount().getLanguage().translate("duels.not_placed_block"));
            event.setCancelled(true);
        } else {
            room.getRollback().remove(event.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        Room room = user.getRoom();

        if (room == null || room.getStage() != RoomStage.PLAYING) {
            event.setCancelled(true);
            return;
        }

        if (!room.getMode().isCanBuild()) {
            event.setCancelled(true);
            return;
        }

        if (room.isOutside(player.getLocation()) || room.isOutside(event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (!user.isPlaying() && !Vanish.getInstance().isVanished(user.getUniqueId())) {
            event.setCancelled(true);
        } else {
            room.getRollback().add(event.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        World world = event.getToBlock().getWorld();

        Room room = instance.getRoomStorage().getRoom(world);

        if (room == null || room.getStage() != RoomStage.PLAYING) {
            event.setCancelled(true);
            return;
        }

        Set<Block> rollbackList = room.getRollback();

        rollbackList.add(event.getToBlock());
        rollbackList.add(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        Room room = user.getRoom();

        if (room == null || room.getStage() != RoomStage.PLAYING) {
            event.setCancelled(true);
            return;
        }

        if (!room.getMode().isCanBuild()) {
            event.setCancelled(true);
            return;
        }

        if (!user.isPlaying() && !Vanish.getInstance().isVanished(user.getUniqueId())) {
            event.setCancelled(true);
        } else {

            Block block = event.getBlockClicked().getRelative(event.getBlockFace());

            if (block.getType() != Material.AIR)
                return;

            room.getRollback().add(block);
        }
    }
}