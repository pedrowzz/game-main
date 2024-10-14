package com.minecraft.arcade.pvp.game.list;

import com.minecraft.arcade.pvp.game.Game;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.bukkit.util.scoreboard.GameScoreboard;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collection;

public class Lava extends Game {

    public Lava() {
        setId(4);
        setType(GameType.LAVA);
        setWorld(Bukkit.getWorld("lava"));
    }

    @Override
    public void handleSidebar(User user) {
        final GameScoreboard gameScoreboard = user.getScoreboard();

        if (gameScoreboard == null)
            return;

        gameScoreboard.updateTitle("§b§lLAVA");

        Collection<String> lines = new ArrayList<>();

        lines.add("§fKills: §70");
        lines.add("§fDeaths: §70");
        lines.add(" ");
        lines.add("§fKillstreak: §a0");
        lines.add(" ");
        lines.add("§fCoins: §60");
        lines.add(" ");
        lines.add("§e" + Constants.SERVER_WEBSITE);

        gameScoreboard.updateLines(lines);
    }

    @Override
    public void onSpawn(User user) {
        this.onPlayerJoinEvent(user, true);
    }

}