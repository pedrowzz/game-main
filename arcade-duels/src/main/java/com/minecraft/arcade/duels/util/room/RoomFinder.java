package com.minecraft.arcade.duels.util.room;

import com.minecraft.arcade.duels.Duels;
import com.minecraft.arcade.duels.game.Game;
import com.minecraft.arcade.duels.room.Arena;
import com.minecraft.core.bukkit.arcade.game.GameQuantity;
import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.bukkit.arcade.route.GameRouteContext;

import java.util.List;
import java.util.stream.Collectors;

public class RoomFinder {

    public static Arena findRoom(GameRouteContext context) {

        GameType gameType = context.getType();
        GameQuantity gameQuantity = context.getQuantity();
        int slots = context.getSlots();
        int mapId = context.getMap();

        Game game = Duels.getInstance().getGame(gameType);

        if (game == null)
            return null;

        Arena foundArena = null;

        List<Arena> availableArenas = game.getRooms().stream().map(room -> (Arena) room).filter(room -> {

            if (!room.isWaiting())
                return false;

            GameQuantity quantity = room.getQuantity();

            if (quantity != GameQuantity.NONE && quantity != gameQuantity)
                return false;

            if (!room.getMap().isFiltered(mapId))
                return false;

            return room.hasSlots(slots);
        }).collect(Collectors.toList());

        if (availableArenas.isEmpty() && game.getRooms().size() < game.getMaxRooms()) {
            foundArena = game.newArena(game.getMap(0));
        } else if (!availableArenas.isEmpty()) {
            availableArenas.sort((a, b) -> Integer.compare(b.getPlayers().size(), a.getPlayers().size()));
            foundArena = availableArenas.get(0);
        }
        return foundArena;
    }
}
