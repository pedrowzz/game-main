package com.minecraft.core.proxy.util.antibot.list;

import com.minecraft.core.proxy.util.antibot.AntiBotModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@AllArgsConstructor
public class AddressLimit extends AntiBotModule {
    @Getter
    private final int limit;

    @Override
    public boolean isViolator(PendingConnection connection) {

        String connectionAddress = connection.getAddress().getHostString();

        int monitorCount = 1;

        for (ProxiedPlayer a : BungeeCord.getInstance().getPlayers()) {
            if (a.getPendingConnection().getAddress().getHostString().equals(connectionAddress))
                monitorCount++;
        }

        return monitorCount > limit;
    }
}
