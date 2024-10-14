package com.minecraft.core.proxy.command;

import com.minecraft.core.account.Account;
import com.minecraft.core.account.AccountStorage;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.annotation.Completer;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.enums.Rank;
import com.minecraft.core.proxy.util.command.ProxyInterface;
import com.minecraft.core.proxy.util.player.PlayerPingHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LatencyCommand implements ProxyInterface {

    @Command(name = "latency", aliases = {"ping"}, platform = Platform.BOTH)
    public void handleCommand(Context<CommandSender> context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            if (context.isPlayer())
                Argument.SELF.getExecutor().execute(context);
            else
                Argument.AVERAGE.getExecutor().execute(context);
        } else {
            Argument argument = Argument.get(args[0]);

            if (!context.getAccount().hasPermission(argument.getRank())) {
                context.info("command.insufficient_permission");
                return;
            }

            argument.getExecutor().execute(context);
        }
    }

    @Completer(name = "latency")
    public List<String> handleComplete(Context<CommandSender> context) {
        if (context.argsCount() == 1) {
            String arg0 = context.getArg(0);
            Account account = Account.fetch(context.getUniqueId());
            List<String> response = Arrays.stream(Argument.values()).filter(c -> c.getArgument() != null && account.hasPermission(c.getRank()) && startsWith(c.getArgument(), arg0)).map(c -> c.getArgument().toLowerCase()).collect(Collectors.toList());
            response.addAll(getOnlineNicknames(context));
            return response;
        }
        return Collections.emptyList();
    }

    @Getter
    @AllArgsConstructor
    public enum Argument {
        TARGET(null, Rank.MEMBER, (context) -> {

            ProxiedPlayer player = getPlayer(context.getArg(0));

            if (player == null) {
                context.info("target.not_found");
                return;
            }

            Account account = Account.fetch(player.getUniqueId());
            final int adminId = Rank.ADMINISTRATOR.getId();

            if (account.getRank().getId() >= adminId && context.getAccount().getRank().getId() < adminId) {
                context.info("target.not_found");
                return;
            }

            PlayerPingHistory pings = account.getProperty("pings", new PlayerPingHistory()).getAs(PlayerPingHistory.class);

            if (pings.size() > 10) {

                int minPing = pings.getMinimum();
                int avgPing = pings.getAverage();
                int maxPing = pings.getMaximum();

                context.info("command.ping.other_ping", account.getDisplayName(), minPing, avgPing, maxPing);
            } else {
                context.info("command.ping.other_ping", account.getDisplayName(), "...", "...", "...");
            }
        }),


        SELF(null, Rank.MEMBER, (context) -> {

            Account account = context.getAccount();
            PlayerPingHistory pings = account.getProperty("pings", new PlayerPingHistory()).getAs(PlayerPingHistory.class);

            if (pings.size() > 10) {
                int minPing = pings.getMinimum();
                int avgPing = pings.getAverage();
                int maxPing = pings.getMaximum();

                context.info("command.ping.self_ping", minPing, avgPing, maxPing);
            } else {
                context.info("command.ping.self_ping", "...", "...", "...");
            }
        }),

        AVERAGE("average", Rank.TRIAL_MODERATOR, (context) -> {

            if (!context.isPlayer()) {
                context.sendMessage("§cInvalid platform.");
                return;
            }

            // Room average

            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) context.getSender();

            List<Integer> pings = new ArrayList<>();

            proxiedPlayer.getServer().getInfo().getPlayers().forEach(p -> {
                Account account = Account.fetch(p.getUniqueId());

                if (account == null)
                    return;

                pings.addAll(account.getProperty("pings").getAs(PlayerPingHistory.class).getPings());
            });

            int avgPing = (int) Math.round(pings.stream().mapToInt(i -> i).average().orElse(-1));

            context.info("command.ping.room_average_ping", avgPing);
            pings.clear();

            // Global average

            BungeeCord.getInstance().getPlayers().forEach(p -> {
                Account account = Account.fetch(p.getUniqueId());

                if (account == null)
                    return;

                pings.addAll(account.getProperty("pings").getAs(PlayerPingHistory.class).getPings());
            });

            avgPing = (int) Math.round(pings.stream().mapToInt(i -> i).average().orElse(-1));

            context.info("command.ping.global_average_ping", avgPing);
            pings.clear();
        });

        private final String argument;
        private final Rank rank;
        private final Executor executor;

        public static Argument get(String key) {
            return Arrays.stream(values()).filter(c -> c.getArgument() != null && c.getArgument().equalsIgnoreCase(key)).findFirst().orElse(TARGET);
        }

        private static ProxiedPlayer getPlayer(String name) {
            Account account = AccountStorage.getAccountByName(name, false);

            if (account != null)
                return ProxyServer.getInstance().getPlayer(account.getUniqueId());
            return null;
        }

        private interface Executor {
            void execute(Context<CommandSender> context);
        }
    }
}
