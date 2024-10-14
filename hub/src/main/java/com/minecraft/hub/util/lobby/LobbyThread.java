package com.minecraft.hub.util.lobby;

import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.util.concurrent.TimeUnit;

public final class LobbyThread implements Runnable {

    public LobbyThread(final boolean daemon) {
        final Thread thread = new Thread(this);

        thread.setDaemon(daemon);
        thread.start();
    }

    @Override
    public void run() {
        while (MinecraftServer.getServer().isRunning()) {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

}