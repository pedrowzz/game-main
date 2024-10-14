package com.minecraft.core.proxy.command;

import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.server.Server;
import com.minecraft.core.server.ServerCategory;
import com.minecraft.core.server.ServerType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TournamentCommand {

    @Command(name = "torneio", aliases = {"tournament"}, platform = Platform.PLAYER)
    public void handleCommand(Context<ProxiedPlayer> context) {

        Server server = ServerCategory.HG.getServerFinder().getBestServer(ServerType.TOURNAMENT);

        if (server == null) {
            context.info("no_server_available", "torneio");
            return;
        }

        ServerInfo proxyServer = ProxyServer.getInstance().getServerInfo(server.getName());

        if (context.getSender().getServer().getInfo().equals(proxyServer)) {
            context.info("already_connected");
            return;
        }

        context.getSender().connect(proxyServer);
    }
}
