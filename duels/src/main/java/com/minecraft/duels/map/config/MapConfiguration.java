package com.minecraft.duels.map.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class MapConfiguration {

    private String name;
    private Location spawnPoint, redLocation, blueLocation;
    private int size, height;

    @Override
    public String toString() {
        return "MapConfiguration{" +
                "name='" + name + '\'' +
                ", spawnPoint=" + spawnPoint +
                ", redLocation=" + redLocation +
                ", blueLocation=" + blueLocation +
                ", size=" + size +
                ", height=" + height +
                '}';
    }
}
