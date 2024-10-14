package com.minecraft.thebridge.game;

import com.minecraft.core.bukkit.server.thebridge.GameType;
import com.minecraft.thebridge.TheBridge;
import com.minecraft.thebridge.game.enums.GameStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class GameStorage {

    private final Map<UUID, Game> games = new HashMap<>();
    private final TheBridge theBridge;

    public Game getGame(final World world) {
        return games.get(world.getUID());
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(theBridge, () -> games.values().forEach(Game::tick), 20, 20);
    }

    public void end() {
        delete(new File(Bukkit.getWorlds().get(0).getWorldFolder(), "playerdata"));
        games.forEach((c, a) -> delete(a.getWorld().getWorldFolder()));
        games.clear();
    }

    public void register(Game game) {
        games.put(game.getWorld().getUID(), game);
        System.out.println("Game " + game.getCode() + " loaded!");
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

    public Game get(GameType gameType) {
        return getGames().values().stream().filter(game -> {

            if (game.getStage() == GameStage.WAITING && game.getType().equals(gameType))
                return !game.isFull();

            return false;
        }).min((a, b) -> Integer.compare(b.getAliveUsers().size(), a.getAliveUsers().size())).orElse(null);
    }

    public List<Game> getGames(GameType gameType) {
        return getGames().values().stream().filter(game -> game.getType().equals(gameType)).collect(Collectors.toList());
    }

    public List<Game> getBusy(GameType gameType) {
        return getGames().values().stream().filter(game -> game.getType().equals(gameType) && game.getStage() != GameStage.WAITING && game.getStage() != GameStage.STARTING).collect(Collectors.toList());
    }

    public TheBridge getTheBridge() {
        return theBridge;
    }

    public Map<UUID, Game> getGames() {
        return games;
    }

    public List<Game> getGameList() {
        return new ArrayList<>(games.values());
    }

}