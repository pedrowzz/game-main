package com.minecraft.core.proxy.command;

import com.minecraft.core.account.Account;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.enums.Rank;
import com.minecraft.core.enums.Tag;
import com.minecraft.core.proxy.util.command.ProxyInterface;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class CreatorlistCommand implements ProxyInterface {

    @Command(name = "creatorlist", aliases = {"cl"}, rank = Rank.PRIMARY_MOD)
    public void handleCommand(Context<ProxiedPlayer> context) {
        final List<Account> accountList = new ArrayList<>();

        for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
            final Account accounts = Account.fetch(players.getUniqueId());

            if (accounts == null) {
                players.disconnect(TextComponent.fromLegacyText("§cNão foi possível processar sua conexão."));
                return;
            }

            final Rank rank = accounts.getRank();

            if (rank.getId() > 4 && rank.getId() < 8)
                accountList.add(accounts);
        }

        if (accountList.size() == 0) {
            context.sendMessage("§cNão há nenhum criador de conteúdo online.");
            return;
        }

        accountList.stream().sorted((a, b) -> Integer.compare(b.getRank().getId(), a.getRank().getId())).forEach(account -> {
            final Tag tag = account.getRank().getDefaultTag();

            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(account.getUniqueId());
            final String server = player.getServer().getInfo().getName();

            final TextComponent serverComponent = new TextComponent(" §f- §7(" + server + ") ");
            final TextComponent textComponent = new TextComponent(tag.getColor() + "§l" + tag.getName().toUpperCase() + " §r" + tag.getColor() + account.getUsername());

            serverComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server));
            serverComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§7Click to go.")}));

            context.getSender().sendMessage(serverComponent, textComponent);
        });

    }


}
