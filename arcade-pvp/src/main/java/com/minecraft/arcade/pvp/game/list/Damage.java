package com.minecraft.arcade.pvp.game.list;

import com.minecraft.arcade.pvp.game.Game;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.bukkit.util.scoreboard.GameScoreboard;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collection;

public class Damage extends Game {

    public Damage() {
        setId(2);
        setType(GameType.DAMAGE);
        setWorld(Bukkit.getWorld("damage"));
    }

    @Override
    public void handleSidebar(User user) {
        final GameScoreboard gameScoreboard = user.getScoreboard();

        if (gameScoreboard == null)
            return;

        gameScoreboard.updateTitle("§b§lDAMAGE");

        Collection<String> lines = new ArrayList<>();

        lines.add(" ");
        lines.add("§e" + Constants.SERVER_WEBSITE);

        gameScoreboard.updateLines(lines);
    }

    @Override
    public void onSpawn(User user) {
        this.onPlayerJoinEvent(user, true);
    }

}