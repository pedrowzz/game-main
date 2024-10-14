package com.minecraft.core.proxy.util.antibot.list;

import com.minecraft.core.proxy.ProxyGame;
import com.minecraft.core.proxy.util.antibot.AntiBotModule;
import net.md_5.bungee.api.connection.PendingConnection;

public class RateLimit extends AntiBotModule {

    private int connections;
    private long expiration;

    @Override
    public boolean isViolator(PendingConnection connection) {

        if (ProxyGame.getInstance().getStartTime() + 15000 > System.currentTimeMillis())
            return false;

        if (isValid()) {
            return connections > 15;
        } else {
            this.connections = 0;
            this.expiration = System.currentTimeMillis() + 500;
        }

        return false;
    }

    private boolean isValid() {
        return System.currentTimeMillis() < expiration;
    }

    public void increment() {
        connections++;
    }

}
