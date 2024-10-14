package com.minecraft.arcade.duels.listeners;

import com.minecraft.arcade.duels.event.block.UserBlockBreakEvent;
import com.minecraft.arcade.duels.event.block.UserBlockPlaceEvent;
import com.minecraft.arcade.duels.room.Arena;
import com.minecraft.arcade.duels.user.User;
import com.minecraft.core.bukkit.arcade.map.rollback.RollbackBlock;
import com.minecraft.core.bukkit.arcade.room.Room;
import com.minecraft.core.bukkit.util.worldedit.Pattern;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class BlockListeners implements Listener {

    @EventHandler
    public void onBlockIgnite(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockFromTo(BlockFromToEvent event) {

        Block block = event.getToBlock();
        World world = block.getWorld();

        if (!world.hasMetadata("arena")) {
            event.setCancelled(true);
            return;
        }

        FixedMetadataValue metadataValue = (FixedMetadataValue) world.getMetadata("arena").get(0);
        Arena room = (Arena) metadataValue.value();
        RollbackBlock rollbackBlock = room.getRollback(block);

        if (!room.hasStarted()) {
            event.setCancelled(true);
            return;
        }

        if(room.getMap().getArea().isOutside(block.getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (rollbackBlock == null) {
            rollbackBlock = new RollbackBlock(block);

            if (block.getType() == Material.AIR) {
                rollbackBlock.setType(RollbackBlock.RollbackType.REMOVE_BLOCK);
            } else {
                rollbackBlock.setType(RollbackBlock.RollbackType.PLACE_BLOCK);
                rollbackBlock.setPattern(Pattern.of(block.getType(), block.getState().getData().getData()));
            }
            room.getRollbackBlocks().add(rollbackBlock);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) { // PLACING LAVA/WATER
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());
        Room room = user.getRoom();

        BlockFace blockFace = event.getBlockFace();
        Vector v = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
        Block block = event.getBlockClicked().getLocation().add(v).getBlock();

        if (room == null) {
            event.setCancelled(true);
        } else if (!room.getGame().isCanBuild(false)) {
            event.setCancelled(true);
        } else if (!room.hasStarted()) {
            event.setCancelled(true);
        } else if (!user.isPlaying()) {
            event.setCancelled(true);
        } else if (room.getMap().getArea().isOutside(block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBucketEmptyMonitor(PlayerBucketEmptyEvent event) {

        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());
        Room room = user.getRoom();
        BlockFace blockFace = event.getBlockFace();
        Vector v = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
        Block block = event.getBlockClicked().getLocation().add(v).getBlock();

        RollbackBlock rollbackBlock = room.getRollback(block);

        if (rollbackBlock == null) {
            rollbackBlock = new RollbackBlock(block);
            rollbackBlock.setType(RollbackBlock.RollbackType.REMOVE_BLOCK);
            room.getRollbackBlocks().add(rollbackBlock);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBucketFillMonitor(PlayerBucketFillEvent event) {

        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());
        Room room = user.getRoom();

        BlockFace blockFace = event.getBlockFace();
        Vector v = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
        Block block = event.getBlockClicked().getLocation().add(v).getBlock();

        RollbackBlock rollbackBlock = room.getRollback(block);

        if (rollbackBlock != null) {
            if (rollbackBlock.getType() == RollbackBlock.RollbackType.REMOVE_BLOCK) {
                room.getRollbackBlocks().remove(rollbackBlock);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) { // GETTING LAVA/WATER
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());
        Room room = user.getRoom();

        BlockFace blockFace = event.getBlockFace();
        Vector v = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
        Block block = event.getBlockClicked().getLocation().add(v).getBlock();

        if (room == null) {
            event.setCancelled(true);
        } else if (!room.getGame().isCanBuild(false)) {
            event.setCancelled(true);
        } else if (!room.hasStarted()) {
            event.setCancelled(true);
        } else if (!user.isPlaying()) {
            event.setCancelled(true);
        } else if (room.getRollback(block) == null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onUserBreakBlockMonitor(UserBlockBreakEvent event) {

        User user = event.getUser();
        Room room = user.getRoom();
        Block block = event.getBlock();
        RollbackBlock rollbackBlock = room.getRollback(block);

        if (rollbackBlock != null) {
            if (rollbackBlock.getType() == RollbackBlock.RollbackType.REMOVE_BLOCK) {
                room.getRollbackBlocks().remove(rollbackBlock);
            }
        } else { // Already on map
            rollbackBlock = new RollbackBlock(block);
            rollbackBlock.setType(RollbackBlock.RollbackType.PLACE_BLOCK);
            rollbackBlock.setPattern(Pattern.of(block.getType(), block.getState().getData().getData()));

            room.getRollbackBlocks().add(rollbackBlock);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onUserPlaceBlockMonitor(UserBlockPlaceEvent event) {

        User user = event.getUser();
        Room room = user.getRoom();
        Block block = event.getBlock();
        RollbackBlock rollbackBlock = room.getRollback(block);

        if (rollbackBlock == null) {
            rollbackBlock = new RollbackBlock(block);
            rollbackBlock.setType(RollbackBlock.RollbackType.REMOVE_BLOCK);
            room.getRollbackBlocks().add(rollbackBlock);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onUserBreakBlockEvent(UserBlockBreakEvent event) {

        User user = event.getUser();
        Room room = user.getRoom();
        Block block = event.getBlock();
        RollbackBlock rollbackBlock = room.getRollback(block);

        if (rollbackBlock == null && !room.getGame().isCanBuild(true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());
        Room room = user.getRoom();

        if (room == null) {
            event.setCancelled(true);
        } else if (!room.getGame().isCanBuild(false)) {
            event.setCancelled(true);
        } else if (!room.hasStarted()) {
            event.setCancelled(true);
        } else if (!user.isPlaying()) {
            event.setCancelled(true);
        } else if (room.getMap().getArea().isOutside(event.getBlock().getLocation())) {
            event.setCancelled(true);
        } else {
            UserBlockPlaceEvent blockPlaceEvent = new UserBlockPlaceEvent(user, event.getBlock(), event.getBlockPlaced(), event.getItemInHand());
            blockPlaceEvent.fire();

            event.setCancelled(blockPlaceEvent.isCancelled());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());
        Room room = user.getRoom();

        if (room == null) {
            event.setCancelled(true);
        } else if (!room.getGame().isCanBuild(false)) {
            event.setCancelled(true);
        } else if (!room.hasStarted()) {
            event.setCancelled(true);
        } else if (!user.isPlaying()) {
            event.setCancelled(true);
        } else if (room.getMap().getArea().isOutside(event.getBlock().getLocation())) {
            event.setCancelled(true);
        } else {

            UserBlockBreakEvent blockBreakEvent = new UserBlockBreakEvent(user, event.getBlock());
            blockBreakEvent.fire();

            event.setCancelled(blockBreakEvent.isCancelled());
            event.setDropItems(blockBreakEvent.isDropItems());
        }
    }
}
