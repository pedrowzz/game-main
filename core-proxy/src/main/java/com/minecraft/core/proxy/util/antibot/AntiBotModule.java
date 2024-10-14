package com.minecraft.core.proxy.util.antibot;

import net.md_5.bungee.api.connection.PendingConnection;

public abstract class AntiBotModule {
    public abstract boolean isViolator(PendingConnection connection);
}
