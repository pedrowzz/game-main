package com.minecraft.core.proxy.util.player;

import java.util.ArrayList;
import java.util.List;

public class PlayerPingHistory {

    private final List<Integer> pings = new ArrayList<>();

    public void addPing(int ping) {
        if (ping == 0)
            return;

        pings.add(0, ping);
        if (pings.size() > 32)
            pings.remove(pings.size() - 1);
    }

    public List<Integer> getPings() {
        return pings;
    }

    public int getAverage() {
        return (int) Math.round(pings.stream().mapToInt(i -> i).average().orElse(-1));
    }

    public int getMinimum() {
        return pings.stream().min(Integer::compare).orElse(-1);
    }

    public int getMaximum() {
        return pings.stream().max(Integer::compare).orElse(-1);
    }

    public int size() {
        return getPings().size();
    }
}
