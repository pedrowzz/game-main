package com.minecraft.bedwars.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;

@Getter
@RequiredArgsConstructor
public enum IslandColor {

    RED("Vermelho", ChatColor.RED),
    BLUE("Azul", ChatColor.BLUE),
    GREEN("Verde", ChatColor.GREEN),
    YELLOW("Amarelo", ChatColor.YELLOW),
    CYAN("Ciano", ChatColor.AQUA),
    WHITE("Branco", ChatColor.WHITE),
    PINK("Rosa", ChatColor.LIGHT_PURPLE),
    GRAY("Cinza", ChatColor.DARK_GRAY);

    private final String name;
    private final ChatColor chatColor;

    @Getter
    private static final IslandColor[] values;

    static {
        values = values();
    }

}