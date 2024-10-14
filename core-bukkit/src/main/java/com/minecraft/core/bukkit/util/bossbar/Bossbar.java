package com.minecraft.core.bukkit.util.bossbar;

import com.minecraft.core.bukkit.util.bossbar.interfaces.BossbarInterface;
import org.bukkit.Location;

public abstract class Bossbar implements BossbarInterface {

    protected static final float MAX_HEALTH = 300;
    protected static final int MAX_DURATION = Integer.MAX_VALUE;

    protected boolean spawned;
    protected Location spawnLocation;

    protected String name;
    protected float health;
    protected int duration;

    public Bossbar(String message, Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        this.name = message;
        this.duration = MAX_DURATION;
        this.health = MAX_HEALTH;
    }

    @Override
    public String getMessage() {
        return name;
    }

    @Override
    public Bossbar setMessage(String message) {
        this.name = message;
        return this;
    }

    @Override
    public float getPercentage() {
        return health / MAX_HEALTH;
    }

    @Override
    public Bossbar setPercentage(float percentage) {
        percentage = clamp(percentage, 0f, 1f);
        health = percentage * MAX_HEALTH;
        return this;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isExpired() {
        return this.duration <= 0;
    }

    public void decreaseDuration() {
        duration--;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    protected <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (value.compareTo(min) < 0) return min;
        else if (value.compareTo(max) > 0) return max;
        else return value;
    }

}
