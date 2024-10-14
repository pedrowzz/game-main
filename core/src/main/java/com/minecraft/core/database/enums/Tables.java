/*
 * Copyright (C) YoloMC, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.minecraft.core.database.enums;

public enum Tables {

    ACCOUNT("accounts", Columns.USERNAME, Columns.RANKS, Columns.PERMISSIONS, Columns.TAGS, Columns.CLANTAGS, Columns.MEDALS, Columns.PUNISHMENTS, Columns.CLAN, Columns.FLAGS, Columns.PREMIUM, Columns.FIRST_LOGIN, Columns.LAST_LOGIN, Columns.ADDRESS, Columns.BANNED, Columns.MUTED),
    AUTH("auth", Columns.PASSWORD, Columns.SESSION_ADDRESS, Columns.SESSION_EXPIRES_AT, Columns.REGISTERED_AT, Columns.PASSWORD_LAST_UPDATE),
    OTHER("other", Columns.NICK, Columns.TAG, Columns.CLANTAG, Columns.LANGUAGE, Columns.PREFIXTYPE, Columns.MEDAL, Columns.PREFERENCES, Columns.SKIN, Columns.BLOCKEDS),
    HUNGERGAMES("hungergames", Columns.HG_KILLS, Columns.HG_DEATHS, Columns.HG_WINS, Columns.HG_GAMES, Columns.HG_MAX_GAME_KILLS, Columns.SCRIM_KILLS, Columns.SCRIM_DEATHS, Columns.SCRIM_WINS, Columns.SCRIM_GAMES, Columns.SCRIM_MAX_GAME_KILLS, Columns.HG_RANK, Columns.HG_RANK_EXP, Columns.SCRIM_RANK, Columns.SCRIM_RANK_EXP, Columns.HG_COINS, Columns.HG_KITS, Columns.HG_DAILY_KITS),
    PVP("pvp", Columns.PVP_ARENA_KILLS, Columns.PVP_ARENA_DEATHS, Columns.PVP_ARENA_KILLSTREAK, Columns.PVP_ARENA_MAX_KILLSTREAK, Columns.PVP_FPS_KILLS, Columns.PVP_FPS_DEATHS, Columns.PVP_FPS_KILLSTREAK, Columns.PVP_FPS_MAX_KILLSTREAK, Columns.PVP_DAMAGE_SETTINGS, Columns.PVP_RANK, Columns.PVP_RANK_EXP, Columns.PVP_COINS, Columns.PVP_KITS, Columns.PVP_DAMAGE_EASY, Columns.PVP_DAMAGE_MEDIUM, Columns.PVP_DAMAGE_HARD, Columns.PVP_DAMAGE_EXTREME),
    THE_BRIDGE("the_bridge", Columns.BRIDGE_SOLO_WINS, Columns.BRIDGE_SOLO_LOSSES, Columns.BRIDGE_SOLO_KILLS, Columns.BRIDGE_SOLO_DEATHS, Columns.BRIDGE_SOLO_POINTS, Columns.BRIDGE_SOLO_ROUNDS, Columns.BRIDGE_SOLO_WINSTREAK, Columns.BRIDGE_SOLO_MAX_WINSTREAK, Columns.BRIDGE_DOUBLES_WINS, Columns.BRIDGE_DOUBLES_LOSSES, Columns.BRIDGE_DOUBLES_KILLS, Columns.BRIDGE_DOUBLES_DEATHS, Columns.BRIDGE_DOUBLES_POINTS, Columns.BRIDGE_DOUBLES_ROUNDS, Columns.BRIDGE_DOUBLES_WINSTREAK, Columns.BRIDGE_DOUBLES_MAX_WINSTREAK, Columns.BRIDGE_COINS, Columns.BRIDGE_RANK, Columns.BRIDGE_RANK_EXP, Columns.BRIDGE_DATA),
    DUELS_BOXING("boxing", Columns.DUELS_BOXING_WINS, Columns.DUELS_BOXING_LOSSES, Columns.DUELS_BOXING_WINSTREAK, Columns.DUELS_BOXING_MAX_WINSTREAK, Columns.DUELS_BOXING_GAMES, Columns.DUELS_BOXING_RATING, Columns.DUELS_BOXING_INVENTORY),
    DUELS_SOUP("soup", Columns.DUELS_SOUP_WINS, Columns.DUELS_SOUP_LOSSES, Columns.DUELS_SOUP_WINSTREAK, Columns.DUELS_SOUP_MAX_WINSTREAK, Columns.DUELS_SOUP_GAMES, Columns.DUELS_SOUP_RATING, Columns.DUELS_SOUP_INVENTORY),
    DUELS_SIMULATOR("simulator", Columns.DUELS_SIMULATOR_WINS, Columns.DUELS_SIMULATOR_LOSSES, Columns.DUELS_SIMULATOR_WINSTREAK, Columns.DUELS_SIMULATOR_MAX_WINSTREAK, Columns.DUELS_SIMULATOR_GAMES, Columns.DUELS_SIMULATOR_RATING, Columns.DUELS_SIMULATOR_INVENTORY),
    DUELS_UHC("uhc", Columns.DUELS_UHC_WINS, Columns.DUELS_UHC_LOSSES, Columns.DUELS_UHC_WINSTREAK, Columns.DUELS_UHC_MAX_WINSTREAK, Columns.DUELS_UHC_GAMES, Columns.DUELS_UHC_RATING, Columns.DUELS_UHC_INVENTORY),
    DUELS_SUMO("sumo", Columns.DUELS_SUMO_WINS, Columns.DUELS_SUMO_LOSSES, Columns.DUELS_SUMO_WINSTREAK, Columns.DUELS_SUMO_MAX_WINSTREAK, Columns.DUELS_SUMO_GAMES, Columns.DUELS_SUMO_RATING, Columns.DUELS_SUMO_INVENTORY),
    DUELS_SCRIM("scrim", Columns.DUELS_SCRIM_WINS, Columns.DUELS_SCRIM_LOSSES, Columns.DUELS_SCRIM_WINSTREAK, Columns.DUELS_SCRIM_MAX_WINSTREAK, Columns.DUELS_SCRIM_GAMES, Columns.DUELS_SCRIM_RATING, Columns.DUELS_SCRIM_INVENTORY),
    DUELS_GLADIATOR("gladiator", Columns.DUELS_GLADIATOR_WINS, Columns.DUELS_GLADIATOR_LOSSES, Columns.DUELS_GLADIATOR_WINSTREAK, Columns.DUELS_GLADIATOR_MAX_WINSTREAK, Columns.DUELS_GLADIATOR_GAMES, Columns.DUELS_GLADIATOR_RATING, Columns.DUELS_GLADIATOR_INVENTORY, Columns.DUELS_GLADIATOR_OLD_RATING, Columns.DUELS_GLADIATOR_OLD_INVENTORY),
    STAFF("staff", Columns.STAFF_WEEKLY_BANS, Columns.STAFF_WEEKLY_EVENTS, Columns.STAFF_WEEKLY_MUTES, Columns.STAFF_MONTHLY_BANS, Columns.STAFF_MONTHLY_EVENTS, Columns.STAFF_MONTHLY_MUTES, Columns.STAFF_LIFETIME_BANS, Columns.STAFF_LIFETIME_EVENTS, Columns.STAFF_LIFETIME_MUTES);

    private final String name;
    private final Columns[] columns;

    Tables(final String name, final Columns... columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public Columns[] getColumns() {
        return columns;
    }
}