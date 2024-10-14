package com.minecraft.core.bukkit.util.listener;

import com.minecraft.core.bukkit.BukkitGame;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@Getter
public abstract class DynamicListener implements Listener {

    private boolean registered = false;

    public void register() {

        if (registered)
            return;

        this.registered = true;
        Bukkit.getPluginManager().registerEvents(this, BukkitGame.getEngine());
    }

    public void unregister() {
        if (!registered)
            return;

        this.registered = false;
        HandlerList.unregisterAll(this);
    }
}
