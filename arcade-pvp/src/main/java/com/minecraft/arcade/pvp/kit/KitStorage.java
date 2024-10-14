package com.minecraft.arcade.pvp.kit;

import com.minecraft.arcade.pvp.PvP;
import com.minecraft.arcade.pvp.command.KitCommand;
import com.minecraft.arcade.pvp.game.util.GameType;
import com.minecraft.arcade.pvp.kit.list.None;
import com.minecraft.core.bukkit.util.map.DuplicateMap;
import com.minecraft.core.bukkit.util.reflection.ClassHandler;
import com.minecraft.core.bukkit.util.variable.VariableStorage;
import com.minecraft.core.bukkit.util.variable.object.Variable;
import com.minecraft.core.enums.Rank;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class KitStorage implements VariableStorage {

    private final List<Kit> kits;
    private final DuplicateMap<Class<? extends Kit>, Class<? extends Kit>> blockedCombinations;

    private KitCommand employer;
    private Kit defaultKit;

    @Variable(name = "game_type", permission = Rank.ADMINISTRATOR)
    public GameType gameType = GameType.DOUBLEKIT;

    public KitStorage() {
        this.kits = new ArrayList<>();
        this.blockedCombinations = new DuplicateMap<>();
        this.loadVariables();
    }

    public void onEnable() {
        getKits().add(new None());

        try {
            for (Class<?> clazz : ClassHandler.getClassesForPackage(PvP.getInstance(), "com.minecraft.arcade.pvp.kit.list")) {
                if (clazz.getSimpleName().equalsIgnoreCase("None"))
                    continue;
                if (Kit.class.isAssignableFrom(clazz)) {
                    Kit kit = (Kit) clazz.newInstance();
                    getKits().add(kit);
                    Bukkit.getPluginManager().registerEvents(kit, PvP.getInstance());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        this.employer = new KitCommand();
        this.defaultKit = kits.get(0);
    }

    public Kit getKit(String name) {
        return getKits().stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Kit getKit(Class<? extends Kit> clazz) {
        return getKits().stream().filter(kit -> kit.getClass() == clazz).findFirst().orElse(null);
    }

    public boolean isBlocked(Class<? extends Kit> kit, Class<? extends Kit> kit2) {
        if (getBlockedCombinations().containsKey(kit) && getBlockedCombinations().get(kit).contains(kit2)) {
            return true;
        } else return getBlockedCombinations().containsKey(kit2) && getBlockedCombinations().get(kit2).contains(kit);
    }

    public Kit getDefaultKit() {
        return this.defaultKit;
    }

    public GameType getGameType() {
        return gameType;
    }

    public List<Kit> getKits() {
        return this.kits;
    }

    public KitCommand getEmployer() {
        return employer;
    }

    public DuplicateMap<Class<? extends Kit>, Class<? extends Kit>> getBlockedCombinations() {
        return blockedCombinations;
    }

}