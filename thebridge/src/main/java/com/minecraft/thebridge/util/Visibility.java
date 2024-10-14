package com.minecraft.thebridge.util;

import com.minecraft.thebridge.game.Game;
import com.minecraft.thebridge.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Visibility {

    public static void refresh(final Player player) {
        final User user = User.fetch(player.getUniqueId());
        final Game game = user.getGame();

        if (game == null) {
            hideAll(player);
            return;
        }

        boolean shouldHide = game.isSpectator(user);

        for (final Player other : Bukkit.getOnlinePlayers()) {
            if (!other.getWorld().getUID().equals(player.getWorld().getUID())) {
                player.hidePlayer(other);
                other.hidePlayer(player);
            } else {
                final User otherUser = User.fetch(other.getUniqueId());

                if (shouldHide) {
                    other.hidePlayer(player);
                }

                if (game.isSpectator(otherUser)) {
                    player.hidePlayer(other);
                    continue;
                }

                player.showPlayer(other);

                if (!shouldHide)
                    other.showPlayer(player);
            }
        }
    }

    public static void refreshWorld(final Player player) {
        final User user = User.fetch(player.getUniqueId());
        final Game game = user.getGame();

        if (game == null) {
            hideAll(player);
            return;
        }

        boolean shouldHide = game.isSpectator(user);

        for (final Player other : player.getWorld().getPlayers()) {
            final User otherUser = User.fetch(other.getUniqueId());

            if (shouldHide) {
                other.hidePlayer(player);
            }

            if (game.isSpectator(otherUser)) {
                player.hidePlayer(other);
                continue;
            }

            player.showPlayer(other);

            if (!shouldHide)
                other.showPlayer(player);
        }
    }

    public static void hideAll(final Player player) {
        for (final Player other : Bukkit.getOnlinePlayers()) {
            other.hidePlayer(player);
            player.hidePlayer(other);
        }
    }

}