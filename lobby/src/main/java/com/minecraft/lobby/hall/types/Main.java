/*
 * Copyright (C) YoloMC, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

package com.minecraft.lobby.hall.types;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.server.hologram.InfoHologram;
import com.minecraft.core.bukkit.util.cooldown.type.Cooldown;
import com.minecraft.core.bukkit.util.hologram.Hologram;
import com.minecraft.core.bukkit.util.leaderboard.Leaderboard;
import com.minecraft.core.bukkit.util.leaderboard.hologram.LeaderboardHologram;
import com.minecraft.core.bukkit.util.leaderboard.libs.LeaderboardType;
import com.minecraft.core.bukkit.util.leaderboard.libs.LeaderboardUpdate;
import com.minecraft.core.bukkit.util.npc.NPC;
import com.minecraft.core.bukkit.util.scoreboard.AnimatedString;
import com.minecraft.core.bukkit.util.scoreboard.GameScoreboard;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.core.enums.Tag;
import com.minecraft.core.server.Server;
import com.minecraft.core.server.ServerCategory;
import com.minecraft.core.server.ServerType;
import com.minecraft.lobby.Lobby;
import com.minecraft.lobby.hall.Hall;
import com.minecraft.lobby.user.User;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Main extends Hall {

    private final Leaderboard bansLeaderboard = new Leaderboard(Columns.STAFF_MONTHLY_BANS, LeaderboardUpdate.HALF_HOUR, LeaderboardType.PLAYER, 20, Columns.USERNAME, Columns.RANKS).query();
    private final Leaderboard mutesLeaderboard = new Leaderboard(Columns.STAFF_MONTHLY_MUTES, LeaderboardUpdate.HALF_HOUR, LeaderboardType.PLAYER, 20, Columns.USERNAME, Columns.RANKS).query();

    private final Location bansLocation, mutesLocation;

    public Main(Lobby lobby) {
        super(lobby, "Main Lobby", "lobby", "JOGANDO NO YOLOMC.COM");

        Constants.setServerType(ServerType.MAIN_LOBBY);
        Constants.setLobbyType(ServerType.MAIN_LOBBY);

        WorldBorder worldBorder = getWorld().getWorldBorder();
        worldBorder.setCenter(getSpawn());
        worldBorder.setSize(550);

        this.bansLocation = new Location(Bukkit.getWorld("world"), 4.5, 73, 6.5);
        this.mutesLocation = new Location(Bukkit.getWorld("world"), -3.5, 73, 6.5);
    }

    @Override
    public void join(User user) {
        super.join(user);
    }

    @Override
    public void handleNPCs(User user) {
        Player player = user.getPlayer();
        boolean staffer = user.getAccount().getRank().isStaffer();

        Bukkit.getScheduler().runTaskLater(getLobby(), () -> {

            HG.clone(player).spawn(true);
            PVP.clone(player).spawn(true);
            DUELS.clone(player).spawn(true);

            InfoHologram pvp = new InfoHologram(player, PVP.getLocation().clone().add(0, 2.1, 0), null, "§bPvP", LeaderboardUpdate.SECOND, () -> Constants.getServerStorage().count(ServerType.PVP_LOBBY, ServerType.PVP));
            pvp.setInteract(interact);
            pvp.show();

            InfoHologram duels = new InfoHologram(player, DUELS.getLocation().clone().add(0, 2.1, 0), null, "§bDuels", LeaderboardUpdate.SECOND, () -> Constants.getServerStorage().count(ServerType.DUELS, ServerType.DUELS_LOBBY));
            duels.setInteract(interact);
            duels.show();

            InfoHologram hg = new InfoHologram(player, HG.getLocation().clone().add(0, 2.1, 0), null, "§BHardcore Games", LeaderboardUpdate.SECOND, () -> Constants.getServerStorage().count(ServerType.HG_LOBBY, ServerType.HGMIX, ServerType.TOURNAMENT, ServerType.CLANXCLAN, ServerType.SCRIM, ServerType.EVENT));
            hg.setInteract(interact);
            hg.show();

            if (staffer) {
                LeaderboardHologram leaderboardHologram6 = new LeaderboardHologram(bansLeaderboard, "§e§lTOP 20 §b§lBANS MENSAL §7(%s/%s)", player, bansLocation);
                leaderboardHologram6.show();

                LeaderboardHologram leaderboardHologram7 = new LeaderboardHologram(mutesLeaderboard, "§e§lTOP 20 §b§lMUTES MENSAL §7(%s/%s)", player, mutesLocation);
                leaderboardHologram7.show();
            }

            new Hologram(player, location, "§6§LPARKOUR", "§eSiga em frente.").show();

        }, (user.getAccount().getVersion() >= 47 ? 0 : 5));
    }

    protected final Location location = new Location(getWorld(), 34.5, 64.5, 29.5);

    @Override
    public void quit(User user) {
        super.quit(user);
    }

    private final AnimatedString animatedString = new AnimatedString(Constants.SERVER_NAME.toUpperCase(), "§e§l", "§6§l", "§b§l");

    @Override
    public void handleSidebar(User user) {
        GameScoreboard gameScoreboard = user.getScoreboard();

        if (gameScoreboard == null)
            return;

        gameScoreboard.updateTitle(getSidebarName());

        if (isPeriodic(20)) {
            List<String> scores = new ArrayList<>();
            Tag tag = user.getAccount().getTagList().getHighestTag();
            int count = Constants.getServerStorage().count();

            scores.add(" ");
            scores.add("§fRank: §r" + tag.getColor() + tag.getName());
            scores.add(" ");
            scores.add("§fLobby: §7#" + getRoom());
            scores.add("§fPlayers: §a" + (count == -1 ? "..." : count));
            scores.add(" ");
            scores.add("§e" + Constants.SERVER_WEBSITE);

            gameScoreboard.updateLines(scores);
        }
    }

    private final NPC HG = NPC.builder().equipment(NPC.Equipment.builder().hand(new ItemStack(Material.MUSHROOM_SOUP)).build()).location(new Location(getSpawn().getWorld(), 0.5, 64.5, 45.5, 180, 0)).property(new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYzNjE5NjA3ODk1NiwKICAicHJvZmlsZUlkIiA6ICI2NDU4Mjc0MjEyNDg0MDY0YTRkMDBlNDdjZWM4ZjcyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaDNtMXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJmMjNiMGQ1NjkyNWEwYTI5YmU0ZDUzMDExODAzODFjMGIxODdmZDRmNTJkOTAyOTA4ODY1MDQxNDdmY2VmZCIKICAgIH0KICB9Cn0=", "Q+M5kRV6Vsl0tgFl08yeulUhJdVN1j8NpaH88w7sQ3qShBvgzwixn0HZN6+Uh9fTLXkmypE8LLXmXQDioXtDBlFfDlIxo1VjfszLsDAP6l2UHrCA67Qeg0N0zVn2NlqapoTIKuL4loa/VnY1BStTIdoKZBLpKMYwY0XBlFwnGIjGVlyLAGNINfrUpH53gf0ugBZi4MtQJzxQkGqQuTOzt30mPWMgR5lhqLj5J5emgiXXFxZQOXOXpkC2S3Q9zk9uPKM31+ekMnvNILlreEA1hV5rU1jlnT3ujVT+5EZqXjmd32QBrWNgm2i7MHc0P5Rd30urH0na7hoB8LzfrbNXj7rHnmxTROC6Ktpnz6S08RE9Z4RvdqA2Z4mlxFvT5pirXCWjEAY0goHXR2HBewTsep6WJNNNyCERH46cNOfQ/oGrFuYujZoyEGr71YacNcbOE84QEz2aIe+a1b937+JHg0Opd65ef/cVLEidgC4bmYyqUk673vEf6Xf6z59WwmFWMgpJedUx6JaWmdCtXKkCT/mxleMlJ72OoZ30xF5Avq0WfWBlaYW3ZNsDugHX5i+JzuNfh0VAyF4ReQDsWMeT3pTeaoZB46eVwDC7REeRkeNH00R4Xr81dda6LD9YTFrUlDHts3tGXPFWWZflEBlSmqcCvXfgBT0vme31DJeYld0=")).interactExecutor((player, npc, type) -> {
        if (isConnectionCooldown(player.getUniqueId())) {
            Account account = Account.fetch(player.getUniqueId());
            Cooldown cooldown = this.getCooldown(player.getUniqueId());

            player.sendMessage(account.getLanguage().translate("wait_to_connect", Constants.SIMPLE_DECIMAL_FORMAT.format(cooldown.getRemaining())));
            return;
        }

        addCooldown(player.getUniqueId());

        Account account = Account.fetch(player.getUniqueId());
        Server server = ServerCategory.LOBBY.getServerFinder().getBestServer(ServerType.HG_LOBBY);

        if (server == null) {
            player.sendMessage(account.getLanguage().translate("no_server_available", "hg"));
            return;
        }

        account.connect(server);
    }).build();

    private final NPC PVP = NPC.builder().equipment(NPC.Equipment.builder().hand(new ItemStack(Material.IRON_CHESTPLATE)).build()).location(new Location(Bukkit.getWorld("world"), 3.5, 64.5, 46.5, 175, 0)).property(new Property("textures", "eyJ0aW1lc3RhbXAiOjE1ODcxNzcyNDk1ODAsInByb2ZpbGVJZCI6IjJhYjJlOWM1MzdlZTQ1MmU4ODAxODY5NmIzYTFmNzZjIiwicHJvZmlsZU5hbWUiOiJNYXJjZWwiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM4MDllZDZmZGU3ZGI5ZGI2ZjIwNjJjN2U0MGQ1MzFhMTA2MGQ3NDIyNjY2YzE4YTdiMzgzYjAxMDk3YmJmODUifSwiQ0FQRSI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U3ZGZlYTE2ZGM4M2M5N2RmMDFhMTJmYWJiZDEyMTYzNTljMGNkMGVhNDJmOTk5OWI2ZTk3YzU4NDk2M2U5ODAifX19", "k0Ziy8pTbPIeDOk4j8724QLeyajUE1YbdjkLKV5MI3QSmibxgZQZC6f9v7hRJhAlj+uoOlu58ormNFdYRij/Mh53o4vWL3BaIknh9D++O0bk069rgVP1szV5anFcGafshW/0JbqudjrEhiTjxEj1Dih8mW1L+Ndz1vadP7C/yb7hfJ/BsSk0ztyJQHyGZk8kykek7Lw5C8HkNJI+9WKIj57GZopEK5jlkNAoLx61/lHx4DeWmw1vdB8xLVc7D81FcnKRe9+vZD8iYieRriiCnLwUa63zZehrUjHYOxPyXgxeYjOQS4ejD4AUwUd9pCkHHKt9nWBjGnabwUijsRB97zIK8iDiE9A8pSkvQ3GG2o8mnTyjLooXjDFukCx9wtARCBPJcsNkOVq46oLSp8ILf8HalO48DCxq57skoLsauktfvf3iFpyF9+zouoIdIHAxpQOpLpugfB41TLnzru2dvBuLvvULD3PpZgrgtCqBcmkVfbL5VO48oy8Y6pTuFMOhhxGq4KdzV1Hv2byH+p5ySY3CxBo7qZukr243uWuE59ERqsnaRyXUJiicY176MA3NnPA36iA4W7EF3wJVuWYv/coBiyBWlYsIgvpiKyAeOuIzOD89QgIml63oPZoc/H75tqIFKcZR55jInLAtZPz9pkcaPD9NN7/rXzgWW9BcQ3I=")).interactExecutor((player, npc, type) -> {
        if (isConnectionCooldown(player.getUniqueId())) {
            Account account = Account.fetch(player.getUniqueId());
            Cooldown cooldown = this.getCooldown(player.getUniqueId());

            player.sendMessage(account.getLanguage().translate("wait_to_connect", Constants.SIMPLE_DECIMAL_FORMAT.format(cooldown.getRemaining())));
            return;
        }

        addCooldown(player.getUniqueId());

        Account account = Account.fetch(player.getUniqueId());
        Server server = ServerCategory.LOBBY.getServerFinder().getBestServer(ServerType.PVP_LOBBY);

        if (server == null) {
            player.sendMessage(account.getLanguage().translate("no_server_available", "pvp"));
            return;
        }

        account.connect(server);
    }).build();

    private final NPC DUELS = NPC.builder().equipment(NPC.Equipment.builder().hand(new ItemStack(Material.DIAMOND_SWORD)).build()).location(new Location(Bukkit.getWorld("world"), -2.5, 64.5, 46.5, -175, 0)).property(new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMTU3NjM4MjY3MSwKICAicHJvZmlsZUlkIiA6ICJkZTE0MGFmM2NmMjM0ZmM0OTJiZTE3M2Y2NjA3MzViYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTUlRlYW0iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmY3OGI3Mjg5Y2JmODJjM2NhMjNkMWJhZGIwMzk2YmFiOWExMzIyOGNmMWFlZTRjYTdjMmM0OTM4ZDE4NjI1ZiIKICAgIH0KICB9Cn0=", "Wsq1dtV+uEEl/6ZDyWX3Qfljt/bXrTMr6X285qXLm39pXvohXJcyf8t1YvJwhSJ9A5FPo9I5QxkJtyAQSii4jqtBZ9dZuDgL6XJdtrR4v/nHfIjxmvLxU8IhqHMB2sbe76xWzQsT6OMPFUwU54kIDrmrgCUpX4DW86Lngee8u+lUbsnQXm8onqATGFMDHhagxUopuYl1iaStvdOQ3sr950OUb7MaBLzhyf6z2XScyw6/fJjJHWXexCD2yvvKN48uJmPXcV/0QeaX0fFb7IT+bHLDhEmk7ge1pP9BnlUIELg04ulEtN/H+WKi74WSZKZ6IKADfKu6vgIKIPE2LtvM0AtnujJu6lx6MjGUYGhT5EZx4Y/Kc87fN2rPMHIjbJ+1wTV4ArWBYfxNNhO2sOILumg90fx9nE7HDsSKK/SUrQN13K8OR0WzsRTOPTMr7tsKAFkhYfud/xemmM0l52ipimC4c5ueL8SX2/YRSyC0wBSEDc65smpHAlAlp0wpenDm+BvbeW7lUN9TYRvohiy0D+uIEMbPrBCuGvMNPhE6cH+aWDHgloODpaH6fIG7oEEZc+o6HgxkPagvZa19PmSFQCynqTpI9iF/jVQ+vnv08bLRUCalDeo26cDgQkYAGlAhbaZliGD8+eIE6MelAUy1kn3KARq0pNDE9NmCEu3uXP4=")).interactExecutor((player, npc, type) -> {
        if (isConnectionCooldown(player.getUniqueId())) {
            Account account = Account.fetch(player.getUniqueId());
            Cooldown cooldown = this.getCooldown(player.getUniqueId());

            player.sendMessage(account.getLanguage().translate("wait_to_connect", Constants.SIMPLE_DECIMAL_FORMAT.format(cooldown.getRemaining())));
            return;
        }

        addCooldown(player.getUniqueId());

        Account account = Account.fetch(player.getUniqueId());
        Server server = ServerCategory.LOBBY.getServerFinder().getBestServer(ServerType.DUELS_LOBBY);

        if (server == null) {
            player.sendMessage(account.getLanguage().translate("no_server_available", "duels"));
            return;
        }

        account.connect(server);
    }).build();

    private final NPC THE_BRIDGE = NPC.builder().equipment(NPC.Equipment.builder().hand(new ItemStack(Material.STAINED_CLAY)).build()).location(new Location(Bukkit.getWorld("world"), 0.5, 63.5, 40.5, 180, 0)).property(new Property("textures", "eyJ0aW1lc3RhbXAiOjE1MzQxMzY3NzA4MTMsInByb2ZpbGVJZCI6IjBiZTU2MmUxNzIyODQ3YmQ5MDY3MWYxNzNjNjA5NmNhIiwicHJvZmlsZU5hbWUiOiJ4Y29vbHgzIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81OGUzZTdmOGY0MWYxOTJmNzQzOWI0YjllOTU2ZDk4ZjQzYzAzOGNiODQwZjIzYWJlYjg1YmI2ZmY2MDBkYjY1In19fQ==", "OJzADdLod8MMXbIEqKdKmqdcOyNh3OuUPXxQOBruCy6rMPiWv8cA7S1mf9YNsERCTj8Fxe3uqnEA3Z9eDt9ROkL3RTg8MQvC18Yr3o+dqriwRRrOwFuFShutTg1vb239Zv3O99YaLYHg6b7+RvBDFUldM9hzlSTsZ9YucUTOLvfS5kA4+n8o9w/ZhIMP045FciNuGHSR8f/HANJLIpa2bXv/38VRnp7V9i9OcPoODctE8YqbZ/MfY5lgkWjVcqn+hrYISkKP1ICABE1+/ns3zL3uvc2FBYQZ0hpO17Y4OlZ4Zi9WQFsD0vGRWOMhgtP4Q4+tq0nH6gqQ6kQYK8rXKMYU0EgShCFtZynFwjSmTOE51lhuxhjYSWGQP1Ux/uK6ltF8CK6bsvcjEZIN8Tyn+GvjsffEv48uIjL/z7hHNVv0gsulUtslcNuikmdbwoMFjFQGbshRzhUDK1LyM9u7n8d42VU1VCzmqYvXM864vj0Ledfrp5GI3UOUlBq1Fdaiw8pbnp1L1tLnaAcl9qOX/EB5KE43/1kkCQVnzF4I9XCMKmIVOh4VXOlrhZiorkXkP+Zm4B3pJU8qg3TsuxDBZQRJXUB8uK3HfySzDim2y57ww12vv0vsgIl/PM8MjvqJ6oP9GfdlCTZiMkLC43HqfYY52iFCU0doVuU/d6R3JDk=")).interactExecutor((player, npc, type) -> {
        if (isConnectionCooldown(player.getUniqueId())) {
            Account account = Account.fetch(player.getUniqueId());
            Cooldown cooldown = this.getCooldown(player.getUniqueId());

            player.sendMessage(account.getLanguage().translate("wait_to_connect", Constants.SIMPLE_DECIMAL_FORMAT.format(cooldown.getRemaining())));
            return;
        }

        addCooldown(player.getUniqueId());

        Account account = Account.fetch(player.getUniqueId());
        Server server = ServerCategory.LOBBY.getServerFinder().getBestServer(ServerType.THE_BRIDGE_LOBBY);

        if (server == null) {
            player.sendMessage(account.getLanguage().translate("no_server_available", "the_bridge"));
            return;
        }

        account.connect(server);
    }).build();

    @Getter
    @Setter
    private String sidebarName = Constants.SERVER_NAME;

    @Override
    public void run() {
        super.run();
        if (isPeriodic(3)) {
            setSidebarName("§f§l" + animatedString.next());
            getLobby().getUserStorage().getUsers().forEach((uuid, user) -> user.handleSidebar());
        }
    }
}