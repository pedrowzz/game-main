package com.minecraft.arcade.duels.room;

import com.minecraft.core.bukkit.arcade.ArcadeGame;
import com.minecraft.core.bukkit.arcade.map.Map;
import com.minecraft.core.bukkit.arcade.room.Room;
import com.minecraft.core.bukkit.arcade.team.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.World;

public class Arena extends Room {

    public Arena(int id, World world, Map map, ArcadeGame<?> game) {
        super(id, world, map, game);

        setTeams(new Team[]{
                new Team("teams.red", ChatColor.RED, game),
                new Team("teams.blue", ChatColor.BLUE, game)
        });
    }

    public boolean hasSlots(int i) {
        int players = getPlayers().size();
        int maxPlayers = getQuantity().getMaxPlayers();
        return (maxPlayers - players) >= i;
    }
}