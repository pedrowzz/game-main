package com.minecraft.core.bukkit.server.thebridge;

import com.minecraft.core.bukkit.util.worldedit.Pattern;
import com.minecraft.core.enums.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BridgeCageConfig {

    private String displayName;
    private Pattern icon;
    private CageRarity rarity;
    private Rank rank;
    private boolean exclusive;
    private int price;

}