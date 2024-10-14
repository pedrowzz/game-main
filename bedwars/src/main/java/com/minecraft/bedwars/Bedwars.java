package com.minecraft.bedwars;

import com.minecraft.core.bukkit.BukkitGame;

public class Bedwars extends BukkitGame {

    private static Bedwars instance;

    @Override
    public void onLoad() {
        super.onLoad();

        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Bedwars getInstance() {
        return instance;
    }

}