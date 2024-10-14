package com.minecraft.core.bukkit.arcade.room;

import com.minecraft.core.bukkit.arcade.ArcadeGame;
import com.minecraft.core.bukkit.arcade.game.GameQuantity;
import com.minecraft.core.bukkit.arcade.map.Map;
import com.minecraft.core.bukkit.arcade.map.rollback.RollbackBlock;
import com.minecraft.core.bukkit.arcade.team.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.github.paperspigot.Title;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Room {

    private final int id;

    private final World world;
    private final Map map;

    private Team[] teams;

    private final ArcadeGame<?> game;
    private GameQuantity quantity = GameQuantity.NONE;
    private RoomStage stage = RoomStage.WAITING;

    private Set<RollbackBlock> rollbackBlocks = new HashSet<>();

    public RollbackBlock getRollback(Block block) {
        return rollbackBlocks.stream().filter(b -> block.equals(b.getBlock())).findFirst().orElse(null);
    }

    public boolean hasStarted() {
        return stage != RoomStage.WAITING && stage != RoomStage.STARTING;
    }

    public boolean isWaiting() {
        return stage != RoomStage.WAITING;
    }

    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        for (Team team : getTeams()) {
            players.addAll(team.getMembers());
        }
        return players;
    }

    public void sendMessage(final String message) {
        this.world.getPlayers().forEach(player -> player.sendMessage(message));
    }

    public void sendTitle(final Title title) {
        this.world.getPlayers().forEach(player -> player.sendTitle(title));
    }

    public void playSound(final Sound sound, final float var, final float var1) {
        this.world.getPlayers().forEach(player -> player.playSound(player.getLocation(), sound, var, var1));
    }
}