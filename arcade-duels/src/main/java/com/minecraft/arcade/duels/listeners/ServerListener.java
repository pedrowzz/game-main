package com.minecraft.arcade.duels.listeners;

import com.minecraft.arcade.duels.Duels;
import com.minecraft.arcade.duels.game.Game;
import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.event.server.ServerPayloadSendEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class ServerListener implements Listener {

    private final String modes;
    private final Set<Game> games;

    public ServerListener() {
        this.modes = Constants.GSON.toJson(Duels.getInstance().getGames().stream().map(Game::getType).collect(Collectors.toList()));
        this.games = Duels.getInstance().getGames();
    }

    @EventHandler
    public void onServerPayloadSend(ServerPayloadSendEvent event) {
        event.getPayload().write("modes", modes);
    }
}
