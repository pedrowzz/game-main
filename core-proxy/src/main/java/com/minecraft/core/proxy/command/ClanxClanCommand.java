package com.minecraft.core.proxy.command;

import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.proxy.util.command.ProxyInterface;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Deprecated
public class ClanxClanCommand implements ProxyInterface {

    @Command(name = "clanxclan", aliases = {"cxc"}, platform = Platform.PLAYER)
    public void handleCommand(Context<ProxiedPlayer> context) {
        BungeeCord.getInstance().getPluginManager().dispatchCommand(context.getSender(), "play clanxclan");
    }

    @Command(name = "clanxclan2", aliases = {"cxc2"}, platform = Platform.PLAYER)
    public void handleClanCommand(Context<ProxiedPlayer> context) {
        ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo("clanxclan2");

        if (serverInfo == null) {
            context.info("server.not_found");
            return;
        }

        context.getSender().connect(serverInfo);
    }

}