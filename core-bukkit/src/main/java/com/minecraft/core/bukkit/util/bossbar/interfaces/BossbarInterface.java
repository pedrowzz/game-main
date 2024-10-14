package com.minecraft.core.bukkit.util.bossbar.interfaces;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;

public interface BossbarInterface {

    /**
     * Returns the message.
     *
     * @return message
     */
    String getMessage();

    /**
     * Set the message.
     *
     * @param message message
     * @return this
     */
    BossbarInterface setMessage(String message);

    /**
     * Returns the amount of health in a percentage of [0~1]. 0 is the minimum value, while 1 is the maximum.
     *
     * @return percentage
     */
    float getPercentage();

    /**
     * Set the amount of health in a percentage of [0~1]. 0 is the minimum value, while 1 is the maximum.
     *
     * @param percentage percentage
     * @return this
     */
    BossbarInterface setPercentage(float percentage);

    /**
     * Returns the spawn packet. Only for internal purposes.
     *
     * @return packet
     */
    Packet getSpawnPacket();

    /**
     * Returns the destroy packet. Only for internal purposes.
     *
     * @return packet
     */
    Packet getDestroyPacket();

    /**
     * Returns the meta packet. Only for internal purposes.
     *
     * @param watcher data watcher
     * @return packet
     */
    Packet getMetaPacket(DataWatcher watcher);

    /**
     * Returns the teleport packet. Only for internal purposes.
     *
     * @param location location
     * @return packet
     */
    Packet getTeleportPacket(Location location);

    /**
     * Returns the data watcher. Only for internal purposes.
     *
     * @return packet
     */
    DataWatcher getWatcher();

}
