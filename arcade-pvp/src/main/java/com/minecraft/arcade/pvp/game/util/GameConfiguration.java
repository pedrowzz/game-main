package com.minecraft.arcade.pvp.game.util;

import com.minecraft.arcade.pvp.game.Game;
import com.minecraft.core.database.enums.Columns;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class GameConfiguration {

    private final Game game;

    private Location spawn;
    private Location hubNpc;

    private List<Columns> columnsList = new ArrayList<>();

    public void addColumns(Columns... columns) {
        this.columnsList.addAll(Arrays.asList(columns));
    }

}