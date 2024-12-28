/*
 * Copyright (C) YoloMC, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

package com.minecraft.core.proxy.command;

import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.enums.Rank;
import com.minecraft.core.proxy.ProxyGame;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import lombok.Getter;

@Getter
@Command(name = "tps", permission = "command.tps", rank = Rank.ADMIN)
public class ProxyTicksPerSecondCommand extends ProxyCommand {

    public ProxyTicksPerSecondCommand(ProxyFrame frame) {
        super(frame, "tps");
    }

    @Override
    public void execute(Context<CommandSender> context) {
        CommandSender sender = context.getSender();
        // Implementação do comando
        sender.sendMessage(ChatColor.GREEN + "TPS: " + getServerTPS());
    }

    private double getServerTPS() {
        // Implementação para calcular TPS
        return 20.0;
    }
}
