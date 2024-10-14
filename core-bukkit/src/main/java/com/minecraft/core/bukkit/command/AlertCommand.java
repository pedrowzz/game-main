package com.minecraft.core.bukkit.command;

import com.minecraft.core.Constants;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.enums.Rank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertCommand {

    @Command(name = "alert", rank = Rank.SECONDARY_MOD)
    public void handleCommand(Context<CommandSender> context, String[] strings) {
        String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", strings));
        Bukkit.broadcastMessage("§b§l" + Constants.getServerType().getName().toUpperCase() + " §7» §r" + message);
    }

    @Command(name = "sound", rank = Rank.SECONDARY_MOD)
    public void handleCommand(Context<Player> context, String sound, float volume, float pitch2) {
        context.getSender().playSound(context.getSender().getLocation(), Sound.valueOf(sound.toUpperCase()), volume, pitch2);
    }

}
