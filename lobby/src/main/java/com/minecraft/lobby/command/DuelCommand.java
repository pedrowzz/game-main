package com.minecraft.lobby.command;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.server.duels.DuelType;
import com.minecraft.core.bukkit.server.route.GameRouteContext;
import com.minecraft.core.bukkit.server.route.PlayMode;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.annotation.Completer;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.payload.ServerRedirect;
import com.minecraft.core.server.Server;
import com.minecraft.core.server.ServerCategory;
import com.minecraft.core.server.ServerType;
import com.minecraft.lobby.Lobby;
import com.minecraft.lobby.duel.Challenge;
import com.minecraft.lobby.duel.DuelMenu;
import com.minecraft.lobby.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DuelCommand implements BukkitInterface {

    @Command(name = "duel", platform = Platform.PLAYER, usage = "{label} <target>")
    public void handleCommand(Context<Player> context, Player target) {
        if (context.argsCount() == 1) {
            Player sender = context.getSender();

            if (target == null || !sender.canSee(target)) {
                context.info("target.not_found");
                return;
            }

            if (sender.getEntityId() == target.getEntityId()) {
                context.info("command.duel.cant_challenge_yourself");
                return;
            }

            new DuelMenu(User.fetch(context.getUniqueId()), User.fetch(target.getUniqueId())).build();
        } else if (context.argsCount() == 2) {
            Player sender = context.getSender();

            if (target == null || !sender.canSee(target)) {
                context.info("target.not_found");
                return;
            }

            if (sender.getEntityId() == target.getEntityId()) {
                context.info("command.duel.cant_challenge_yourself");
                return;
            }

            DuelType duelType = DuelType.fromName(context.getArg(1));

            if (duelType == null) {
                context.info("target.not_found");
                return;
            }

            duel(User.fetch(context.getUniqueId()), User.fetch(target.getUniqueId()), duelType);
        }
    }

    @Completer(name = "duel")
    public List<String> handleComplete(Context<CommandSender> context) {
        if (context.argsCount() == 1)
            return getOnlineNicknames(context);
        return Collections.emptyList();
    }

    private static void connect(Account account, UUID target, DuelType duelType, Server server) {

        GameRouteContext gameRouteContext = new GameRouteContext();

        gameRouteContext.setGame(duelType);
        gameRouteContext.setTarget(target);
        gameRouteContext.setPlayMode(PlayMode.PLAYER);

        ServerRedirect.Route route = new ServerRedirect.Route(server, Constants.GSON.toJson(gameRouteContext));
        account.connect(new ServerRedirect(account.getUniqueId(), route));
    }

    @AllArgsConstructor
    @Getter
    public enum DuelIcons {

        SOUP_1V1(DuelType.SOUP_1V1, new ItemFactory(Material.MUSHROOM_SOUP).setName("§aSoup 1v1")),
        GLADIATOR_1V1(DuelType.GLADIATOR_1V1, new ItemFactory(Material.IRON_FENCE).setName("§aGladiator 1v1")),
        GLADIATOR_OLD_1V1(DuelType.GLADIATOR_OLD_1V1, new ItemFactory(Material.WOOD_SWORD).setName("§aGladiator Old 1v1")),
        SIMULATOR_1V1(DuelType.SIMULATOR_1V1, new ItemFactory(Material.WEB).setName("§aSimulator 1v1")),
        UHC_1V1(DuelType.UHC_1V1, new ItemFactory(Material.GOLDEN_APPLE).setName("§aUHC 1v1")),
        SUMO_1V1(DuelType.SUMO_1V1, new ItemFactory(Material.LEASH).setName("§aSumo 1v1")),
        BOXING_1V1(DuelType.BOXING_1V1, new ItemFactory(Material.DIAMOND_SWORD).setName("§aBoxing 1v1")),
        SCRIM_1V1(DuelType.SCRIM_1V1, new ItemFactory(Material.INK_SACK).setDurability(3).setName("§aScrim 1v1"));

        private final DuelType duelType;
        private final ItemFactory itemStack;

    }

    public static void duel(User sender, User target, DuelType duelType) {

        Player player = sender.getPlayer();

        if (target.alreadyInvited(sender.getUniqueId(), duelType)) {

            Server server = ServerCategory.DUELS.getServerFinder().getBestServer(ServerType.DUELS);

            connect(target.getAccount(), sender.getUniqueId(), duelType, server);
            Bukkit.getScheduler().runTaskLater(Lobby.getLobby(), () -> connect(sender.getAccount(), target.getUniqueId(), duelType, server), 2L);
        } else if (sender.alreadyInvited(target.getUniqueId(), duelType)) {
            player.sendMessage("§cJá há um convite ativo para esse jogador.");
        } else {
            Challenge challenge = new Challenge(sender, target, duelType);

            sender.getChallenges().add(challenge);
            target.getChallenges().add(challenge);

            player.playSound(sender.getPlayer().getLocation(), Sound.ORB_PICKUP, 4F, 4F);
            player.sendMessage("§aVocê desafiou " + target.getPlayer().getName() + " para " + duelType.getName() + ".");

            Player challenged = target.getPlayer();

            challenged.sendMessage("§aVocê foi desafiado para " + duelType.getName() + " por " + player.getName() + ".");

            notify(target.getPlayer(), sender.getName(), duelType);
        }
    }

    private static void notify(Player player, String name, DuelType duelType) {
        TextComponent c = new TextComponent("§b§lCLIQUE AQUI§e para aceitar o convite.");
        c.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel " + name + " " + duelType.name()));
        player.spigot().sendMessage(c);
    }
}
