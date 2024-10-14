package com.minecraft.thebridge.user;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.server.route.BridgeRouteContext;
import com.minecraft.core.bukkit.server.route.PlayMode;
import com.minecraft.core.bukkit.util.vanish.Vanish;
import com.minecraft.core.enums.Ranking;
import com.minecraft.core.translation.Language;
import com.minecraft.core.util.ranking.RankingFactory;
import com.minecraft.thebridge.TheBridge;
import com.minecraft.thebridge.game.Game;
import com.minecraft.thebridge.game.enums.GameStage;
import com.minecraft.thebridge.util.Visibility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class UserLoader implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerSpawnLocationEvent event) {

        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            player.kickPlayer("§cRota inválida (Destino não encontrado) (fail join)");
            return;
        }

        event.setSpawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
        user.setPlayer(player);

        BridgeRouteContext context = user.getRouteContext();
        UUID target = context.getTarget();

        if (target == null) { // Player is searching for the best room through the queue.

            Game game = TheBridge.getInstance().getGameStorage().get(context.getGameType());

            if (game == null)
                return;

            event.setSpawnLocation(game.getConfiguration().getSpawnPoint());
            game.join(user, context.getPlayMode(), false);

        } else if (context.getPlayMode() == PlayMode.PLAYER) {

            List<Game> games = new ArrayList<>(TheBridge.getInstance().getGameStorage().getGameList());

            Iterator<Game> iterator = games.iterator();
            Game game = null;

            while (iterator.hasNext()) {
                Game iteratorGame = iterator.next();

                if (iteratorGame.getStage() != GameStage.WAITING || iteratorGame.getType() != context.getGameType()) {
                    iterator.remove();
                    continue;
                }

                if (iteratorGame.getWorld().getPlayers().size() != 0) {
                    if (!iteratorGame.isLock() || !iteratorGame.getLock().equals(user.getUniqueId())) {
                        iterator.remove();
                        continue;
                    }
                }

                if (iteratorGame.isLock()) {
                    if (iteratorGame.getLock().equals(user.getUniqueId())) {
                        game = iteratorGame;
                        game.setCountStats(false);
                        break;
                    }
                }
            }

            if (game == null) {

                if (games.isEmpty()) {
                    Bukkit.getScheduler().runTaskLater(TheBridge.getInstance(), user::lobby, 1L);
                    return;
                }

                game = games.get(0);
                game.setLock(target);
            }

            event.setSpawnLocation(game.getConfiguration().getRedLocation());
            game.join(user, PlayMode.PLAYER, false);

            if (game.getLock().equals(user.getUniqueId()))
                game.setLock(null);
        } else {

            Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null)
                return;

            User targetUser = User.fetch(target);
            Game game = targetUser.getGame();

            if (game == null) {
                user.lobby();
                return;
            }

            event.setSpawnLocation(game.getConfiguration().getRedLocation());
            game.join(user, PlayMode.VANISH, false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            player.kickPlayer(Language.PORTUGUESE.translate("unexpected_error"));
            return;
        }

        if (user.getGame() == null) {
            user.lobby();
            return;
        }

        Visibility.refresh(player);

        if (Vanish.getInstance().isVanished(player.getUniqueId()))
            player.setGameMode(GameMode.CREATIVE);
        else
            player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        UUID uniqueId = event.getUniqueId();
        Account account = Account.fetch(uniqueId);

        if (account == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "unexpected_error");
            return;
        }

        try (Jedis jedis = Constants.getRedis().getResource()) {
            String route_key = "route:" + account.getUniqueId();

            String route = jedis.get(route_key);

            if (route == null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, account.getLanguage().translate("arcade.route.not_found", "no route"));
                return;
            }

            BridgeRouteContext routeContext;

            if (Constants.isUniqueId(route)) {
                UUID targetUUID = UUID.fromString(route);

                User user = User.fetch(targetUUID);

                if (user == null || !user.isPlaying()) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cO alvo não está em jogo.");
                    return;
                }

                routeContext = new BridgeRouteContext();

                routeContext.setGameType(user.getGame().getType());
                routeContext.setTarget(targetUUID);
                routeContext.setPlayMode(PlayMode.VANISH);
            } else {
                routeContext = Constants.GSON.fromJson(route, BridgeRouteContext.class);
            }

            jedis.del(route_key);
            TheBridge.getInstance().getUserStorage().storeIfAbsent(account, routeContext);
        }

        RankingFactory factory = TheBridge.getInstance().getRankingFactory();

        if (factory != null) {
            account.setRanking(Ranking.fromId(account.getData(factory.getTarget().getRanking()).getAsInt()));

            if (factory.getBestPlayers().contains(account.getUniqueId()) && account.getRanking() == Ranking.MASTER_IV)
                account.setRanking(Ranking.CHALLENGER);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        final User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) return;

        final Game game = user.getGame();

        if (game == null) return;

        game.quit(user);

        TheBridge.getInstance().getUserStorage().forget(event.getPlayer().getUniqueId());
    }

}