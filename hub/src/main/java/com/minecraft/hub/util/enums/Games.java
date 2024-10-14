package com.minecraft.hub.util.enums;

import com.minecraft.core.server.ServerType;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum Games {

    HUNGER_GAMES("HG", Material.MUSHROOM_SOUP, ServerType.HG_LOBBY, ServerType.HGMIX, ServerType.SCRIM, ServerType.EVENT, ServerType.CLANXCLAN, ServerType.TOURNAMENT),
    DUELS("Duels (The Bridge/Gladiator)", Material.DIAMOND_SWORD, ServerType.DUELS_LOBBY, ServerType.DUELS, ServerType.THE_BRIDGE_LOBBY, ServerType.THE_BRIDGE),
    PVP("PvP", Material.IRON_CHESTPLATE, ServerType.PVP_LOBBY, ServerType.PVP),
    THE_BRIDGE("The Bridge", Material.STAINED_CLAY, ServerType.THE_BRIDGE_LOBBY, ServerType.THE_BRIDGE);

    private final String name;
    private final Material material;
    private final ServerType[] serverTypes;

    Games(final String name, final Material material, final ServerType... serverTypes) {
        this.name = name;
        this.material = material;
        this.serverTypes = serverTypes;
    }

    @Getter
    private static final Games[] values;

    static {
        values = values();
    }

}