package com.minecraft.duels.command;

import com.minecraft.core.bukkit.server.duels.DuelType;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.enums.Rank;
import com.minecraft.duels.Duels;
import org.bukkit.command.CommandSender;

public class DemandCommand {

    @Command(name = "checkdemand", platform = Platform.BOTH, rank = Rank.ADMINISTRATOR)
    public void handleCommand(Context<CommandSender> context) {

        context.sendMessage("§eTotal de salas: §b" + Duels.getInstance().getRoomStorage().getBusy().size() + "§e/§b" + Duels.getInstance().getRoomStorage().getRooms().size());

        for (DuelType duelType : DuelType.values()) {
            context.sendMessage("§6[" + duelType.getName() + "] §eTotal de salas: §b" + Duels.getInstance().getRoomStorage().getBusy(duelType).size() + "§e/§b" + Duels.getInstance().getRoomStorage().getRooms(duelType).size());
        }
    }
}