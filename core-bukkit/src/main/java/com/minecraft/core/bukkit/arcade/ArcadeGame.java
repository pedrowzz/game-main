package com.minecraft.core.bukkit.arcade;

import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.bukkit.arcade.map.Map;
import com.minecraft.core.bukkit.arcade.room.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
@RequiredArgsConstructor
public abstract class ArcadeGame<T> {

    private final T plugin;
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    private String name = getClass().getSimpleName();

    private final Integer minRooms, maxRooms;
    private final String mapDirectory;
    private final GameType type;

    private final Set<Room> rooms = new HashSet<>();

    private final List<Map> maps = new ArrayList<>();

    public abstract boolean load();

    public abstract void unload();

    public abstract Room newArena(Map map);

    @Override
    public String toString() {
        return "ArcadeGame{" +
                "name='" + name + '\'' +
                ", minRooms=" + minRooms +
                ", maxRooms=" + maxRooms +
                ", type=" + type +
                '}';
    }

    public static AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    public Map getMap(int id) {
        return getMaps().stream().filter(map -> map.getId() == id).findFirst().orElse(null);
    }

    public boolean isCanBuild(boolean blockMap) {
        return !blockMap;
    }
}