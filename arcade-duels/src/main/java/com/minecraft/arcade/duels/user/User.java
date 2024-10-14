package com.minecraft.arcade.duels.user;

import com.minecraft.arcade.duels.Duels;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.arcade.room.Room;
import com.minecraft.core.bukkit.arcade.route.GameRouteContext;
import com.minecraft.core.bukkit.arcade.team.Team;
import com.minecraft.core.bukkit.util.scoreboard.GameScoreboard;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
public class User {

    private final Account account;

    private GameScoreboard scoreboard;
    private Player player;

    private Room room;
    private Team team;
    private GameRouteContext routeContext;

    public User(final Account account, final GameRouteContext context) {
        this.account = account;
        this.routeContext = context;
    }

    public static User fetch(UUID uniqueId) {
        return Duels.getInstance().getUserStorage().getUser(uniqueId);
    }

    public boolean isPlaying() {
        return team != null;
    }
}