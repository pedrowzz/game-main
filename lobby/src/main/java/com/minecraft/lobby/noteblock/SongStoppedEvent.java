package com.minecraft.lobby.noteblock;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SongStoppedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final SongPlayer song;

    public SongStoppedEvent(SongPlayer song) {
        this.song = song;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public SongPlayer getSongPlayer() {
        return song;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
