package com.minecraft.thebridge.team;

import com.minecraft.thebridge.game.Game;
import com.minecraft.thebridge.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Team {

    private final String name;
    private final ChatColor chatColor;
    private final List<User> users;
    private Game holder;

    private int points = 0;

    public Team(final Game holder, final String name, final ChatColor chatColor) {
        this.holder = holder;
        this.name = name;
        this.chatColor = chatColor;
        this.users = new ArrayList<>();
    }

    public boolean isFull() {
        return users.size() == holder.getMaxPlayers() / 2;
    }

    public void addPoint() {
        this.points++;
    }

    public void clear() {
        getUsers().clear();
        setPoints(0);
    }

    public List<User> getUserList() {
        return new ArrayList<>(users);
    }
}