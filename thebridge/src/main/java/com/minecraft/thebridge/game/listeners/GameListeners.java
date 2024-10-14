package com.minecraft.thebridge.game.listeners;

import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.hologram.Hologram;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.core.database.enums.Tables;
import com.minecraft.core.util.MessageUtil;
import com.minecraft.thebridge.TheBridge;
import com.minecraft.thebridge.event.GameEndEvent;
import com.minecraft.thebridge.event.GameStartEvent;
import com.minecraft.thebridge.game.Game;
import com.minecraft.thebridge.team.Team;
import com.minecraft.thebridge.util.Items;
import com.minecraft.thebridge.util.Visibility;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.github.paperspigot.Title;

import java.util.ArrayList;
import java.util.List;

public class GameListeners implements Listener, BukkitInterface {

    @EventHandler
    public void onGameStart(final GameStartEvent event) {
        final Game game = event.getGame();

        final boolean countStats = game.isCountStats();

        game.sendMessage("§a§m                                                                        ");
        game.sendMessage(center("§b§lThe Bridge"));
        game.sendMessage("");
        game.sendMessage(center("§ePasse pela ponte para marcar pontos."));
        game.sendMessage(center("§eDerrube seu oponente para ganhar."));
        game.sendMessage("");
        game.sendMessage(center("§eO jogador que pontuar §65§e vezes, ganha!"));
        game.sendMessage("");
        game.sendMessage("§a§m                                                                        ");

        final Location blue = game.getConfiguration().getBlueHologram();
        final Location red = game.getConfiguration().getRedHologram();

        game.getAliveUsers().forEach(user -> {
            final Player player = user.getPlayer();
            final ChatColor chatColor = user.getTeam().getChatColor();

            game.handleSidebar(player);

            Visibility.refreshWorld(player);

            Objective objective = player.getScoreboard().registerNewObjective("showHealth", Criterias.HEALTH);
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName(ChatColor.RED + "❤");

            new Hologram(player, blue, chatColor == ChatColor.BLUE ? ("§9§lSeu Objetivo" + "\n" + "§7§oDefenda!") : ("§9§lObjetivo Azul" + "\n" + "§e§oPule para marcar!")).show();
            new Hologram(player, red, chatColor == ChatColor.RED ? ("§c§lSeu Objetivo" + "\n" + "§7§oDefenda!") : ("§c§lObjetivo Vermelho" + "\n" + "§e§oPule para marcar!")).show();

            if (!countStats)
                player.sendMessage("§c§lESSA PARTIDA NÃO CONTA ESTATÍSTICAS.");
        });

        game.getConfiguration().getBlueBlockPortals().forEach(block -> block.setType(Material.ENDER_PORTAL));
        game.getConfiguration().getRedBlockPortals().forEach(block -> block.setType(Material.ENDER_PORTAL));
    }

    @EventHandler
    public void onGameEnd(final GameEndEvent event) {
        final Game game = event.getGame();
        final boolean countStats = game.isCountStats();

        if (event.getReason() == GameEndEvent.Reason.TIME) {
            List<Team> teamList = new ArrayList<>();

            teamList.add(game.getBlue());
            teamList.add(game.getRed());

            teamList.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));

            if (teamList.isEmpty()) { // WTF?
                game.sendMessage("§cA partida atingiu o tempo limite. Ninguém ganhou :(");
                return;
            }

            event.setWinner(teamList.get(0));
            event.setLoser(teamList.get(1));

            teamList.clear();
        }

        final Team winner = event.getWinner();
        final Team loser = event.getLoser();

        final Location location = game.getConfiguration().getBlueLocation();

        winner.getUsers().forEach(user -> {
            final Player player = user.getPlayer();

            player.teleport(location);

            player.getInventory().setArmorContents(null);
            player.getInventory().clear();

            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());

            Items.find(user.getAccount().getLanguage()).build(player);
            Visibility.refreshWorld(player);

            if (countStats) {
                final Account account = user.getAccount();

                account.addInt(1, game.getType().getWins());
                account.addInt(1, game.getType().getWinstreak());

                if (account.getData(game.getType().getWinstreak()).getAsInt() > account.getData(game.getType().getMaxWinstreak()).getAsInt())
                    account.getData(game.getType().getMaxWinstreak()).setData(account.getData(game.getType().getWinstreak()).getAsInt());

                account.addInt(6, Columns.BRIDGE_RANK_EXP);
                TheBridge.getInstance().getRankingFactory().verify(account);

                player.sendMessage("§b+6 XP");
            }
        });

        loser.getUsers().forEach(user -> {
            final Player player = user.getPlayer();

            player.teleport(location);

            player.getInventory().setArmorContents(null);
            player.getInventory().clear();

            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());

            Items.find(user.getAccount().getLanguage()).build(player);
            Visibility.refreshWorld(player);

            if (countStats) {
                final Account account = user.getAccount();

                account.addInt(1, game.getType().getLosses());
                account.getData(game.getType().getWinstreak()).setData(0);

                account.removeInt(4, Columns.BRIDGE_RANK_EXP);
                TheBridge.getInstance().getRankingFactory().verify(account);

                player.sendMessage("§4-4 XP");
            }
        });

        game.sendMessage("§a§m                                                                        ");
        game.sendMessage(center("§b§lThe Bridge"));
        game.sendMessage("");
        game.sendMessage(center(winner.getChatColor() + "§l" + winner.getName().toUpperCase() + " GANHOU!"));
        game.sendMessage(center(winner.getChatColor() + "§l" + winner.getPoints() + " §7- " + loser.getChatColor() + "§l" + loser.getPoints()));
        game.sendMessage("");
        game.sendMessage("§a§m                                                                        ");

        game.sendTitle(new Title("§6§lVITÓRIA", winner.getChatColor() + winner.getName() + " §eganhou a partida!", 1, 60, 10));

        game.getWorld().getPlayers().forEach(player -> Account.fetch(player.getUniqueId()).getDataStorage().saveTable(Tables.THE_BRIDGE));
    }

    protected final String center(final String message) {
        return MessageUtil.makeCenteredMessage(message);
    }

}