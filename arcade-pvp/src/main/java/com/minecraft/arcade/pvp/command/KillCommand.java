package com.minecraft.arcade.pvp.command;

import com.minecraft.arcade.pvp.event.user.LivingUserDieEvent;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.annotation.Completer;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.enums.Rank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class KillCommand implements BukkitInterface {

    @Command(name = "finalkill", aliases = {"kill"}, platform = Platform.PLAYER, rank = Rank.SECONDARY_MOD)
    public void finalKillCommand(final Context<Player> context, final Player target) {
        final Player sender = context.getSender();

        if (target == null || !sender.canSee(target)) {
            context.info("target.not_found");
            return;
        }

        final User targetUser = User.fetch(target.getUniqueId());

        new LivingUserDieEvent(targetUser, User.fetch(sender.getUniqueId()), LivingUserDieEvent.DieCause.KILL, targetUser.getInventoryContents()).fire();
    }

    @Completer(name = "finalkill")
    public List<String> handleComplete(final Context<CommandSender> context) {
        if (context.argsCount() == 1)
            return getOnlineNicknames(context);
        return Collections.emptyList();
    }

}
