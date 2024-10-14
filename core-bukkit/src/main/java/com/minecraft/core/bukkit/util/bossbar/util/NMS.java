package com.minecraft.core.bukkit.util.bossbar.util;

import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;

public final class NMS {

    private NMS() {
    }

    public static void sendPacket(Player player, Packet... packets) {

        if (player == null)
            return;

        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        if (playerConnection == null)
            return;

        for (Packet packet : packets) {
            if (packet != null) {
                try {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                } catch (Exception ignored) {
                    System.out.println("§cFailed to send a bossbar packet to " + player.getName() + " (" + packet.getClass().getSimpleName() + ")");
                }
            }
        }
    }

    public static void registerCustomEntity(String entityName, Class<?> entityClass, int entityId) {
        Reflections.getField(EntityTypes.class, "c", Map.class).get(null).put(entityName, entityClass);
        Reflections.getField(EntityTypes.class, "d", Map.class).get(null).put(entityClass, entityName);
        Reflections.getField(EntityTypes.class, "e", Map.class).get(null).put(entityId, entityClass);
        Reflections.getField(EntityTypes.class, "f", Map.class).get(null).put(entityClass, entityId);
        Reflections.getField(EntityTypes.class, "g", Map.class).get(null).put(entityName, entityId);
    }

}
