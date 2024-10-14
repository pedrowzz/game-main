package com.minecraft.core.proxy.util.reconnect;

import com.minecraft.core.proxy.ProxyGame;
import com.minecraft.core.server.Server;
import com.minecraft.core.server.ServerType;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class ReconnectTask {

    private final UserConnection user;

    private ScheduledTask schedule;

    private int tries = 0;

    public ReconnectTask(UserConnection user) {
        this.user = user;
    }

    public void run() {
        String serverCurrent = this.user.getServer().getInfo().getName();
        this.schedule = ProxyGame.getInstance().getProxy().getScheduler().schedule(ProxyGame.getInstance(), () -> {
            this.tries++;
            if (!isUserOnline() || (this.tries > 1 && this.user.getServer() != null)) {
                cancel();
                return;
            }
            this.user.setServer(null);

            ServerType serverType = ServerType.MAIN_LOBBY;
            Server server = serverType.getServerCategory().getServerFinder().getBestServer(serverType);

            if (server != null) {
                ServerInfo proxyServer = ProxyServer.getInstance().getServerInfo(server.getName());
                if (!serverCurrent.equals(proxyServer.getName()))
                    this.user.connect(proxyServer, ServerConnectEvent.Reason.SERVER_DOWN_REDIRECT);
                cancel();
                return;
            }

            if (this.tries == 6) {
                cancel();
            }
        }, 0L, 5L, TimeUnit.SECONDS);
    }

    private void cancel() {
        if (this.schedule != null) {
            this.schedule.cancel();
        }
    }

    private boolean isUserOnline() {
        return (ProxyServer.getInstance().getPlayer(this.user.getUniqueId()) != null);
    }

}