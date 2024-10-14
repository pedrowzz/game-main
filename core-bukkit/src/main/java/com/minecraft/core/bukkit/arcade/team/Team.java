package com.minecraft.core.bukkit.arcade.team;

import com.minecraft.core.bukkit.arcade.ArcadeGame;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class Team {

    private final String name;
    private final ChatColor chatColor;

    private final ArcadeGame<?> holder;

    private final Set<Player> members = new HashSet<>();
}