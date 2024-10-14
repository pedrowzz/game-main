package com.minecraft.core.bukkit.event.player;

import com.minecraft.core.bukkit.event.handler.ServerEvent;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.Set;

@Getter
public class PlayerMassiveTeleportExecuteEvent extends ServerEvent implements Cancellable {

    private Set<Player> recipients;
    private Location location;
    private boolean cancelled;

    public PlayerMassiveTeleportExecuteEvent(Set<Player> recipients, Location location) {
        this.recipients = recipients;
        this.location = location;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setRecipients(Set<Player> recipients) {
        this.recipients = recipients;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
