package com.minecraft.hub;

import com.minecraft.core.bukkit.BukkitGame;
import com.minecraft.core.bukkit.server.BukkitServerStorage;
import com.minecraft.core.server.ServerType;
import com.minecraft.hub.command.BuildCommand;
import com.minecraft.hub.command.FlyCommand;
import com.minecraft.hub.lobby.Lobby;
import com.minecraft.hub.selectors.GameSelector;
import com.minecraft.hub.selectors.LobbySelector;
import com.minecraft.hub.user.storage.UserStorage;
import com.minecraft.hub.util.lobby.LobbyStorage;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;

@Getter
public class Hub extends BukkitGame {

    private static Hub instance;

    private Lobby lobby;
    private UserStorage userStorage;

    private LobbySelector lobbySelector;
    private GameSelector gameSelector;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.userStorage = new UserStorage();

        getEngine().getBukkitFrame().registerCommands(new BuildCommand(), new FlyCommand());

        this.lobby = loadLobby();

        BukkitServerStorage bukkitServerStorage = ((BukkitServerStorage) getServerStorage());
        bukkitServerStorage.listen(ServerType.values());
        bukkitServerStorage.subscribeProxyCount();

        startServerDump();

        this.lobbySelector = new LobbySelector(this);
        this.gameSelector = new GameSelector(this);

        //Bukkit.getScheduler().runTaskTimerAsynchronously(this, new UserLastLocationTask(this.userStorage), 10L, 40L);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getWorlds().forEach(world -> deleteFile(new File(world.getWorldFolder(), "playerdata")));
    }

    protected Lobby loadLobby() {
        try {
            Lobby lobby = (Lobby) LobbyStorage.getHall(getConfig().getString("lobby.mode")).getConstructor(Hub.class).newInstance(this);

            lobby.loadVariables();
            lobby.loadListeners();
            lobby.removeRecipes();

            return lobby;
        } catch (Exception exception) {
            exception.printStackTrace();
            Bukkit.shutdown();
            return null;
        }
    }

    public static Hub getInstance() {
        return instance;
    }

}