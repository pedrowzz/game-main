package com.minecraft.core.bukkit.arcade.map;

import com.google.gson.JsonObject;
import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.bukkit.arcade.map.area.Cuboid;
import com.minecraft.core.bukkit.util.file.FileUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Map {

    private final int id;

    private final String name;
    private final GameType gameType;

    private final transient File source;

    private final transient JsonObject configuration;

    private final transient int buildLimit;

    private Cuboid area;

    private final transient List<SignedLocation> signedLocations = new ArrayList<>();

    public boolean isFiltered(final int anInt) {
        return this.id == anInt || anInt == -1;
    }

    public boolean copyTo(final File destination) {
        try {
            FileUtil.copy(this.source, destination, null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public SignedLocation getLocation(String name) {
        return signedLocations.stream().filter(location -> location.getTitle().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void setArea(Cuboid mapArea) {
        this.area = mapArea;
    }
}