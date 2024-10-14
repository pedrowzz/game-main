/*
 * Copyright (C) YoloMC, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

package com.minecraft.core.bukkit.command;

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
import java.util.UUID;

public class SmiteCommand implements BukkitInterface {

    @Command(name = "smite", usage = "{label} <target>", platform = Platform.PLAYER, rank = Rank.ADMINISTRATOR)
    public void handleCommand(Context<Player> context, Player target) {
        Player sender = context.getSender();

        if (target == null || !sender.canSee(target)) {
            context.info("target.not_found");
            return;
        }

        if (isDev(target.getUniqueId())) {
            target = sender;
        }

        for (int i = 0; i < 15; i++) {
            target.getWorld().strikeLightningEffect(target.getLocation().clone().add(randomize(5), 0, randomize(5)));
        }

        context.info("command.smite.successful", target.getName());
    }

    @Completer(name = "smite")
    public List<String> handleComplete(Context<CommandSender> context) {
        if (context.argsCount() == 1)
            return getOnlineNicknames(context);
        return Collections.emptyList();
    }

    protected boolean isDev(UUID uuid) {
        return uuid.equals(UUID.fromString("71112bd0-8419-4b49-9c80-443c0063ee56")) || uuid.equals(UUID.fromString("3448ae86-dd35-42f8-a854-8b4b4a104e54"));
    }

}