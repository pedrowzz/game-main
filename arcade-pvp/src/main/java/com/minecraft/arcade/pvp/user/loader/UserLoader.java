package com.minecraft.arcade.pvp.user.loader;

import com.minecraft.arcade.pvp.PvP;
import com.minecraft.arcade.pvp.game.Game;
import com.minecraft.arcade.pvp.game.util.GameStorage;
import com.minecraft.arcade.pvp.kit.Kit;
import com.minecraft.arcade.pvp.kit.KitStorage;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.arcade.pvp.user.UserStorage;
import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.arcade.route.GameRouteContext;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.scoreboard.GameScoreboard;
import com.minecraft.core.translation.Language;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInitialSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.UUID;

public class UserLoader implements Listener, BukkitInterface {

    private final UserStorage userStorage;
    private final GameStorage gameStorage;
    private final KitStorage kitStorage;

    private final Kit kit;

    public UserLoader() {
        this.userStorage = PvP.getInstance().getUserStorage();
        this.gameStorage = PvP.getInstance().getGameStorage();
        this.kitStorage = PvP.getInstance().getKitStorage();

        this.kit = kitStorage.getDefaultKit();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        UUID uuid = event.getUniqueId();
        Account account = Account.fetch(uuid);

        if (account == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Language.PORTUGUESE.translate("arcade.route.not_found", "no route"));
            return;
        }

        final User user = new User(account);

        try (Jedis redis = Constants.getRedis().getResource()) {

            String route = redis.get("route:" + uuid);

            if (route == null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, account.getLanguage().translate("arcade.route.not_found", "no route"));
                return;
            }

            final GameRouteContext routeContext = Constants.GSON.fromJson(route, GameRouteContext.class);

            user.setRouteContext(routeContext);

            final Game game = this.gameStorage.getGame(routeContext.getType());

            if (game == null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, account.getLanguage().translate("arcade.route.not_found", "no route"));
                return;
            }

            user.setGame(game);
        }

        user.setKits(new Kit[kitStorage.getGameType().getMaxKits()]);

        Arrays.fill(user.getKits(), kit);

        account.getDataStorage().loadColumns(user.getGame().getConfiguration().getColumnsList());

        this.userStorage.store(uuid, user);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSpawn(PlayerInitialSpawnEvent event) {

        final Player player = event.getPlayer();
        final User user = User.fetch(player.getUniqueId());

        final Game game = this.gameStorage.getGame(user.getRouteContext().getType());

        if (game == null) {
            player.kickPlayer("arcade.room.not_found");
            return;
        }

        event.setSpawnLocation(game.getConfiguration().getSpawn());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        final Player player = event.getPlayer();

        final World world = player.getWorld();

        if (!world.hasMetadata("game")) {
            player.kickPlayer("arcade.room.not_found");
            return;
        }

        final User user = User.fetch(player.getUniqueId());

        user.setPlayer(player);
        user.setScoreboard(new GameScoreboard(player));

        final Game game = (Game) world.getMetadata("game").get(0).value();

        game.onPlayerJoinEvent(user, false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        final UUID uniqueId = event.getPlayer().getUniqueId();

        final User user = User.fetch(uniqueId);

        user.getGame().onPlayerQuitEvent(user);

        this.userStorage.forget(uniqueId);
    }

}