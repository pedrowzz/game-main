package com.minecraft.arcade.pvp.util;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventUtils {
    public static boolean isBothPlayers(EntityDamageByEntityEvent event) {
        return event.getEntity() instanceof Player && event.getDamager() instanceof Player;
    }
}