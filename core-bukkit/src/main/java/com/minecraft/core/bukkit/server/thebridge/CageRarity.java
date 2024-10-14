package com.minecraft.core.bukkit.server.thebridge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CageRarity {

    COMMON("Comum", "§a"),
    RARE("Raro", "§b"),
    EPIC("Épico", "§5"),
    LEGENDARY("Lendário", "§4");

    private final String name;
    private final String color;

    public final String getDisplayName() {
        return this.color + this.name;
    }

}
