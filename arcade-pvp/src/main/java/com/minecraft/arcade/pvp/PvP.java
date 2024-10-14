package com.minecraft.arcade.pvp;

import com.minecraft.arcade.pvp.command.BuildCommand;
import com.minecraft.arcade.pvp.command.KillCommand;
import com.minecraft.arcade.pvp.command.SpawnCommand;
import com.minecraft.arcade.pvp.game.util.GameStorage;
import com.minecraft.arcade.pvp.kit.KitStorage;
import com.minecraft.arcade.pvp.listeners.DamageListener;
import com.minecraft.arcade.pvp.listeners.DeathListeners;
import com.minecraft.arcade.pvp.listeners.ServerListeners;
import com.minecraft.arcade.pvp.listeners.SignListener;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.arcade.pvp.user.UserStorage;
import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.BukkitGame;
import com.minecraft.core.bukkit.command.InfoCommand;
import com.minecraft.core.server.ServerType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.UUID;

@Getter
public class PvP extends BukkitGame {

    private static PvP instance;

    private UserStorage userStorage;
    private GameStorage gameStorage;
    private KitStorage kitStorage;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.gameStorage = new GameStorage();
        this.gameStorage.onEnable();

        this.kitStorage = new KitStorage();
        this.kitStorage.onEnable();

        this.userStorage = new UserStorage();
        this.userStorage.onEnable();

        getServer().getPluginManager().registerEvents(new ServerListeners(), this);
        getServer().getPluginManager().registerEvents(new DeathListeners(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);

        getBukkitFrame().registerCommands(new SpawnCommand(), new KillCommand(), new BuildCommand());
        getBukkitFrame().registerCommands(new InfoCommand<>(User.class, s -> User.fetch(UUID.fromString(s))));

        Constants.setServerType(ServerType.UNKNOWN);
        Constants.setLobbyType(ServerType.PVP_LOBBY);
        Constants.getServerStorage().listen(ServerType.PVP_LOBBY);

        startServerDump();

        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        shapelessRecipe.addIngredient(Material.BOWL);
        shapelessRecipe.addIngredient(Material.INK_SACK, 3);

        Bukkit.addRecipe(shapelessRecipe);

        getKitStorage().getEmployer().run(false);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static PvP getInstance() {
        return instance;
    }

}