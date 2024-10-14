package com.minecraft.core.bukkit.listener;

import com.minecraft.core.bukkit.util.BukkitInterface;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.imanity.imanityspigot.packet.PacketHandler;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
public class AntiDamageIndicator implements PacketHandler, BukkitInterface {

    private boolean enabled = true;

/*    @Override
    public boolean onSent(Player player, Object packet) {

        if (packet instanceof PacketPlayOutEntityMetadata) {
            try {

                PacketPlayOutEntityMetadata metadata = (PacketPlayOutEntityMetadata) packet;
                int entityId = (int) getValue(metadata, "a");
                List<DataWatcher.WatchableObject> list = (List<DataWatcher.WatchableObject>) getValue(metadata, "b");
                Entity entity = null;

                for (Entity iterator : player.getWorld().getEntities()) {
                    if (iterator.getEntityId() != entityId)
                        continue;
                    entity = iterator;
                }

                if (player == entity || !(entity instanceof LivingEntity))
                    return true;

                if (entity instanceof EnderDragon || entity instanceof Wither)
                    return true;

                if (entity.getPassenger() == player)
                    return true;

                DataWatcher dataWatcher = ((CraftEntity) entity).getHandle().getDataWatcher();
                DataWatcher newWatcher = new DataWatcher((net.minecraft.server.v1_8_R3.Entity) getValue(dataWatcher, "a"));

                Int2ObjectMap dataValues = (Int2ObjectMap) getValue(dataWatcher, "dataValues");

                for (int i : dataValues.keySet()) {
                    newWatcher.a(i, ((DataWatcher.WatchableObject) dataValues.get(i)).b());
                }

                newWatcher.watch(6, 0.0);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(entity.getEntityId(), newWatcher, false));
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }*/

    private Object getValue(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    private void setValue(Object packet, String fieldName, Object value) {
        try {
            Field f = packet.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(packet, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
