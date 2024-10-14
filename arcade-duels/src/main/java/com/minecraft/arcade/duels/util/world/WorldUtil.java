package com.minecraft.arcade.duels.util.world;

import net.minecraft.server.v1_8_R3.ChunkProviderServer;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.imanity.imanityspigot.chunk.AsyncPriority;

import java.util.Set;

public class WorldUtil {

    public static void adjust(World world, Set<Chunk> chunks, AsyncPriority priority) {

        world.setPVP(true);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("naturalRegeneration", "false");
        world.setGameRuleValue("sendCommandFeedback", "false");
        world.setGameRuleValue("logAdminCommands", "false");

        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(Integer.MIN_VALUE);
        world.setThunderDuration(Integer.MIN_VALUE);

        world.setSpawnLocation(0, 71, 0);
        world.setAutoSave(false);
        world.setTime(6000);

        ChunkProviderServer chunkProvider = ((CraftWorld) world).getHandle().chunkProviderServer;

        chunks.forEach(chunk -> chunkProvider.getChunkAt(chunk.getX(), chunk.getZ(), priority, null));
        world.getEntities().forEach(Entity::remove);
    }

}
