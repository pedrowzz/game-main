package com.minecraft.arcade.duels;

import com.minecraft.arcade.duels.game.Game;
import com.minecraft.arcade.duels.listeners.BlockListeners;
import com.minecraft.arcade.duels.listeners.ServerListener;
import com.minecraft.arcade.duels.user.UserStorage;
import com.minecraft.arcade.duels.user.loader.UserLoader;
import com.minecraft.arcade.duels.util.game.GameFinder;
import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.BukkitGame;
import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.server.ServerType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Duels extends BukkitGame {

    private static Duels instance;

    private UserStorage userStorage;

    private final Set<Game> games = new HashSet<>();

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (getPluginUpdater().isUpdated())
            return;

        Constants.setServerType(ServerType.DOUBLEKIT);
        Constants.setLobbyType(ServerType.DUELS_LOBBY);

        this.userStorage = new UserStorage();

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new BlockListeners(), this);
        pluginManager.registerEvents(new ServerListener(), this);
        pluginManager.registerEvents(new UserLoader(), this);

        getServerStorage().listen(ServerType.MAIN_LOBBY, ServerType.DUELS_LOBBY);

        loadGames();
        startServerDump();
    }

    protected void loadGames() {
        FileConfiguration fileConfiguration = getConfig();
        ConfigurationSection section = fileConfiguration.getConfigurationSection("modes");

        for (String modeName : section.getKeys(false)) {

            final Class<?> unknownClass = GameFinder.find(modeName);

            if (Game.class.isAssignableFrom(unknownClass)) {
                try {
                    final int min_rooms = section.getInt(modeName + ".min_rooms");
                    final int max_rooms = section.getInt(modeName + ".max_rooms");
                    final String mapDirectory = section.getString(modeName + ".directory");

                    Game game = (Game) unknownClass.getConstructor(Duels.class, Integer.class, Integer.class, String.class).newInstance(this, min_rooms, max_rooms, mapDirectory);

                    if (game.load()) {
                        games.add(game);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                    Bukkit.shutdown();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getGames().forEach(Game::unload);
    }

    public static Duels getInstance() {
        return instance;
    }

    public Game getGame(GameType gameType) {
        return getGames().stream().filter(game -> game.getType() == gameType).findFirst().orElse(null);
    }
}