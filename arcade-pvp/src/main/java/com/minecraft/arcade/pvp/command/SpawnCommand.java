package com.minecraft.arcade.pvp.command;

import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.vanish.Vanish;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import org.bukkit.entity.Player;

public class SpawnCommand implements BukkitInterface {

    @Command(name = "spawn", platform = Platform.PLAYER)
    public void handleCommand(final Context<Player> playerContext) {
        final boolean vanished = Vanish.getInstance().isVanished(playerContext.getUniqueId());

        final User user = User.fetch(playerContext.getUniqueId());

        if (!vanished && user.getCombatTag().isTagged()) {
            playerContext.info("command.spawn.in_combat");
            return;
        }

        user.getGame().onSpawn(user);
    }

}