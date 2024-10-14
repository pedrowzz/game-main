package com.minecraft.duels.room.storage;

import com.minecraft.core.bukkit.server.duels.DuelType;
import com.minecraft.duels.Duels;
import com.minecraft.duels.room.Room;
import com.minecraft.duels.util.enums.RoomStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class RoomStorage {

    private final Map<UUID, Room> rooms = new HashMap<>();
    private final Duels duels;

    public Room getRoom(World world) {
        return rooms.get(world.getUID());
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(duels, () -> rooms.values().forEach(c -> c.getMode().tick(c)), 20, 20);
    }

    public void end() {
        delete(new File(Bukkit.getWorlds().get(0).getWorldFolder(), "playerdata"));
        rooms.forEach((c, a) -> delete(a.getWorld().getWorldFolder()));
        rooms.clear();
    }

    public void register(Room room) {
        rooms.put(room.getWorld().getUID(), room);
    }

    public void delete(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            for (String child : children) {
                delete(new File(file, child));
            }
        }
        if (file.exists())
            file.delete();
    }

    public Room get(DuelType duelType) {
        return getRooms().values().stream().filter(room -> {

            if (room.isLock())
                return false;

            if (room.getStage() == RoomStage.WAITING && room.getMode().getSupportedModes().contains(duelType)) {
                return !room.isFull();
            }

            return false;
        }).min((a, b) -> Integer.compare(b.getAlivePlayers().size(), a.getAlivePlayers().size())).orElse(null);
    }

    public List<Room> getRooms(DuelType duelType) {
        return getRooms().values().stream().filter(room -> room.getMode().getSupportedModes().contains(duelType)).collect(Collectors.toList());
    }

    public List<Room> getBusy(DuelType duelType) {
        return getRooms().values().stream().filter(room -> room.getMode().getSupportedModes().contains(duelType) && room.getStage() != RoomStage.WAITING && room.getStage() != RoomStage.STARTING).collect(Collectors.toList());
    }

    public List<Room> getBusy() {
        return getRooms().values().stream().filter(room -> room.getStage() != RoomStage.WAITING && room.getStage() != RoomStage.STARTING).collect(Collectors.toList());
    }

    public Duels getDuels() {
        return duels;
    }
}
