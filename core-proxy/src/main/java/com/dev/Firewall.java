package com.yolo.dev;

import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;

public class Firewall {
    private static final Firewall instance = new Firewall();
    private final Set<SocketAddress> firewall = new HashSet<>();

    private Firewall() {}

    public static Firewall getInstance() {
        return instance;
    }

    public void addFirewall(SocketAddress address) {
        firewall.add(address);
    }

    public Set<SocketAddress> getFirewall() {
        return firewall;
    }

    public boolean isFirewalled(SocketAddress address) {
        return firewall.contains(address);
    }
}