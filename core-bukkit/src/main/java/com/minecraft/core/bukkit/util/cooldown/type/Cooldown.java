package com.minecraft.core.bukkit.util.cooldown.type;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Cooldown {

    @Getter
    @Setter
    @NonNull
    private String displayName, key;
    @Getter
    @Setter
    private boolean display;

    @Getter
    private double duration;
    @Getter
    private long startTime = System.currentTimeMillis();

    public Cooldown(String displayname, String key, double duration, boolean display) {
        this.displayName = displayname;
        this.key = key;
        this.duration = duration;
        this.display = display;
    }

    public void update(double duration, long startTime) {
        this.duration = duration;
        this.startTime = startTime;
    }

    public double getPercentage() {
        return (getRemaining() * 100) / duration;
    }

    public double getRemaining() {
        long endTime = (long) (startTime + duration * 1000);
        return (-(System.currentTimeMillis() - endTime)) / 1000D;
    }

    public boolean expired() {
        return getRemaining() <= 0D;
    }

}