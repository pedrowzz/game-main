package com.minecraft.pvp.commands;

import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.annotation.Completer;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.enums.Rank;
import com.minecraft.pvp.event.UserDiedEvent;
import com.minecraft.pvp.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class KillCommand implements BukkitInterface {

    @Command(name = "finalkill", aliases = {"kill"}, platform = Platform.PLAYER, rank = Rank.SECONDARY_MOD)
    public void finalKillCommand(Context<Player> context, final Player target) {
        Player sender = context.getSender();

        if (target == null || !sender.canSee(target)) {
            context.info("target.not_found");
            return;
        }

        new UserDiedEvent(User.fetch(target.getUniqueId()), User.fetch(sender.getUniqueId()), Collections.emptyList(), target.getLocation(), UserDiedEvent.Reason.KILL, User.fetch(target.getUniqueId()).getGame()).fire();
    }

    @Completer(name = "finalkill")
    public List<String> handleComplete(Context<CommandSender> context) {
        if (context.argsCount() == 1)
            return getOnlineNicknames(context);
        return Collections.emptyList();
    }

}
