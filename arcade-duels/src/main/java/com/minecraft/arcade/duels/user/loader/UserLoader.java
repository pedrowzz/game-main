package com.minecraft.arcade.duels.user.loader;

import com.minecraft.arcade.duels.Duels;
import com.minecraft.arcade.duels.room.Arena;
import com.minecraft.arcade.duels.user.User;
import com.minecraft.arcade.duels.user.UserStorage;
import com.minecraft.arcade.duels.util.room.RoomFinder;
import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.arcade.game.GameQuantity;
import com.minecraft.core.bukkit.arcade.map.rollback.RollbackBlock;
import com.minecraft.core.bukkit.arcade.room.Room;
import com.minecraft.core.bukkit.arcade.room.RoomStage;
import com.minecraft.core.bukkit.arcade.route.GameRouteContext;
import com.minecraft.core.bukkit.util.BukkitInterface;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class UserLoader implements Listener, BukkitInterface {

    private final UserStorage userStorage;

    public UserLoader() {
        this.userStorage = Duels.getInstance().getUserStorage();
    }

    @EventHandler
    public void onPlayerOvo(AsyncPlayerChatEvent event) {

        User user = User.fetch(event.getPlayer().getUniqueId());
        Room room = user.getRoom();

        event.setCancelled(true);

        if (event.getMessage().equals("start")) {
            sync(() -> room.setStage(RoomStage.PLAYING));
        } else if (event.getMessage().equals("rollback")) {
            sync(() -> {

                room.getRollbackBlocks().forEach(rollbackBlock -> {

                    if (rollbackBlock.getType() == RollbackBlock.RollbackType.REMOVE_BLOCK)
                        rollbackBlock.getBlock().setType(Material.AIR);
                    else {
                        rollbackBlock.getBlock().setType(rollbackBlock.getPattern().getMaterial());
                        rollbackBlock.getBlock().setData(rollbackBlock.getPattern().getData());
                    }

                });

                room.getRollbackBlocks().clear();

            });
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {

        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());
        user.setPlayer(player);

        GameRouteContext routeContext = user.getRouteContext();

        if (routeContext.getType() != null) {
            Arena arena = RoomFinder.findRoom(routeContext);

            if (arena == null) {
                player.kickPlayer("arcade.room.not_found");
                return;
            }

            if (arena.getQuantity() == GameQuantity.NONE)
                arena.setQuantity(routeContext.getQuantity());

            user.setRoom(arena);

            arena.getTeams()[0].getMembers().add(player);
            user.setTeam(arena.getTeams()[0]);
            event.setSpawnLocation(arena.getMap().getLocation("spawn").getLocation().getBukkitLocation(arena.getWorld()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {

        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        UUID uuid = event.getUniqueId();
        Account account = Account.fetch(uuid);

        if (account == null) {
            return;
        }

        try (Jedis redis = Constants.getRedis().getResource()) {

            String route = redis.get("route:" + uuid);

            if (route == null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, account.getLanguage().translate("arcade.route.not_found", "no route"));
                return;
            }

            GameRouteContext routeContext = Constants.GSON.fromJson(route, GameRouteContext.class);

            //TODO: Load columns

            userStorage.store(uuid, new User(account, routeContext));

        } catch (Exception e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, account.getLanguage().translate("arcade.route.not_found", "fail join"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        userStorage.forget(event.getPlayer().getUniqueId());
    }
}
