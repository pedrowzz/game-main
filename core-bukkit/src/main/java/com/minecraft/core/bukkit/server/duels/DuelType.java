package com.minecraft.core.bukkit.server.duels;

import com.minecraft.core.database.enums.Columns;
import com.minecraft.core.database.enums.Tables;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DuelType {

    BOXING_1V1(2, Tables.DUELS_BOXING, "Boxing 1v1", Columns.DUELS_BOXING_INVENTORY),
    BOXING_2V2(4, Tables.DUELS_BOXING, "Boxing 2v2", Columns.DUELS_BOXING_INVENTORY),
    GLADIATOR_1V1(2, Tables.DUELS_GLADIATOR, "Gladiator 1v1", Columns.DUELS_GLADIATOR_INVENTORY),
    GLADIATOR_2V2(4, Tables.DUELS_GLADIATOR, "Gladiator 2v2", Columns.DUELS_GLADIATOR_INVENTORY),
    SCRIM_1V1(2, Tables.DUELS_SCRIM, "Scrim 1v1", Columns.DUELS_SCRIM_INVENTORY),
    SCRIM_2V2(4, Tables.DUELS_SCRIM, "Scrim 2v2", Columns.DUELS_SCRIM_INVENTORY),
    GLADIATOR_OLD_1V1(2, Tables.DUELS_GLADIATOR, "Gladiator Old 1v1", Columns.DUELS_GLADIATOR_OLD_INVENTORY),
    GLADIATOR_OLD_2V2(4, Tables.DUELS_GLADIATOR, "Gladiator Old 2v2", Columns.DUELS_GLADIATOR_OLD_INVENTORY),
    SOUP_1V1(2, Tables.DUELS_SOUP, "Soup 1v1", Columns.DUELS_SOUP_INVENTORY),
    SOUP_2V2(4, Tables.DUELS_SOUP, "Soup 2v2", Columns.DUELS_SOUP_INVENTORY),
    UHC_1V1(2, Tables.DUELS_UHC, "UHC 1v1", Columns.DUELS_UHC_INVENTORY),
    UHC_2V2(4, Tables.DUELS_UHC, "UHC 2v2", Columns.DUELS_UHC_INVENTORY),
    SUMO_1V1(2, Tables.DUELS_SUMO, "Sumo 1v1", Columns.DUELS_SUMO_INVENTORY),
    SUMO_2V2(4, Tables.DUELS_SUMO, "Sumo 2v2", Columns.DUELS_SUMO_INVENTORY),
    SIMULATOR_1V1(2, Tables.DUELS_SIMULATOR, "Simulator 1v1", Columns.DUELS_SIMULATOR_INVENTORY),
    SIMULATOR_2V2(4, Tables.DUELS_SIMULATOR, "Simulator 2v2", Columns.DUELS_SIMULATOR_INVENTORY);

    private final int maxPlayers;
    private final Tables table;
    private final String name;
    private final Columns inventory;

    @Getter
    private static final DuelType[] values;

    static {
        values = values();
    }

    public static DuelType fromName(String name) {
        return Arrays.stream(getValues()).filter(duelType -> duelType.name().equals(name)).findFirst().orElse(null);
    }

}