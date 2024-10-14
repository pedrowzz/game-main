package com.minecraft.core.bukkit.command;

import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.enums.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.spigotmc.TicksPerSecondCommand;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class TpsCommand implements BukkitInterface {

    @Command(name = "tps", rank = Rank.ADMINISTRATOR)
    public void handleCommand(final Context<CommandSender> context) {
        final CommandSender sender = context.getSender();

        final double[] tps = org.bukkit.Bukkit.spigot().getTPS();

        String[] tpsAvg = new String[tps.length];

        for (int i = 0; i < tps.length; i++) {
            tpsAvg[i] = format(tps[i]);
        }

        final String fullServerTick = formatMs(co.aikar.timings.TimingsManager.FULL_SERVER_TICK.getAverage());
        final String worldTick = formatMs(co.aikar.timings.SpigotTimings.worldsTimer.getAverage());
        final int currentOnline = org.bukkit.Bukkit.getOnlinePlayers().size();
        final int maxOnline = org.bukkit.Bukkit.getMaxPlayers();
        final String uptime = formatDate(System.currentTimeMillis() - START_MILLIS);

        final org.bukkit.World currentWorld = sender instanceof org.bukkit.entity.Player ? ((org.bukkit.entity.Player) sender).getWorld() : org.bukkit.Bukkit.getWorlds().get(0);
        final int entities = currentWorld.getEntities().size();
        final int loadedChunks = currentWorld.getLoadedChunks().length;

        final long usedMemory = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 2) / 1048576L;
        final long allocatedMemory = Runtime.getRuntime().totalMemory() / 1048576L;

        final int availableProcessors = BEAN.getAvailableProcessors();
        final String processCpuLoad = FORMAT.format(BEAN.getProcessCpuLoad() * 100);

        sender.sendMessage(ChatColor.GREEN + "TPS (1m, 5m, 15m, 5s, 10s): " + ChatColor.RED + org.apache.commons.lang.StringUtils.join(tpsAvg, ", "));
        sender.sendMessage(ChatColor.GREEN + "Online: " + ChatColor.WHITE + currentOnline + "/" + maxOnline);
        sender.sendMessage(ChatColor.GREEN + "Memory: " + ChatColor.WHITE + usedMemory + "/" + allocatedMemory + " MB");
        sender.sendMessage(ChatColor.GREEN + "Uptime: " + ChatColor.WHITE + uptime);
        sender.sendMessage(ChatColor.GREEN + "Entities: " + ChatColor.WHITE + entities);

        if (context.getAccount().hasPermission(Rank.DEVELOPER_ADMIN)) {
            sender.sendMessage(ChatColor.GREEN + "Logic Tick Time: " + ChatColor.WHITE + fullServerTick);
            sender.sendMessage(ChatColor.GREEN + "World Tick Time: " + ChatColor.WHITE + worldTick);
            sender.sendMessage(ChatColor.GREEN + "Loaded chunks: " + ChatColor.WHITE + loadedChunks);
            sender.sendMessage(ChatColor.GREEN + "Cores: " + ChatColor.WHITE + availableProcessors);
            sender.sendMessage(ChatColor.GREEN + "Loads: " + ChatColor.WHITE + processCpuLoad + "%");
        }
    }

    private static final java.text.DecimalFormat FORMAT = new java.text.DecimalFormat("0.0");
    private static final String OPERATING_SYSTEM_BEAN = "java.lang:type=OperatingSystem";

    private static final TicksPerSecondCommand.OperatingSystemMXBean BEAN;

    static {
        try {
            MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName diagnosticBeanName = ObjectName.getInstance(OPERATING_SYSTEM_BEAN);
            BEAN = JMX.newMXBeanProxy(beanServer, diagnosticBeanName, TicksPerSecondCommand.OperatingSystemMXBean.class);
            BEAN.getAvailableProcessors();
        } catch (Exception e) {
            throw new UnsupportedOperationException("OperatingSystemMXBean is not supported by the system", e);
        }
    }

    private static String formatDate(long millis) {
        double seconds = (double) Math.max(0, millis) / 1000;
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;
        double weeks = days / 7;
        double months = days / 31;
        double years = months / 12;


        if (years >= 1) {
            return FORMAT.format(years) + " year" + (years != 1 ? "s" : "");
        } else if (months >= 1) {
            return FORMAT.format(months) + " month" + (months != 1 ? "s" : "");
        } else if (weeks >= 1) {
            return FORMAT.format(weeks) + " week" + (weeks != 1 ? "s" : "");
        } else if (days >= 1) {
            return FORMAT.format(days) + " day" + (days != 1 ? "s" : "");
        } else if (hours >= 1) {
            return FORMAT.format(hours) + " hour" + (hours != 1 ? "s" : "");
        } else if (minutes >= 1) {
            return FORMAT.format(minutes) + " minute" + (minutes != 1 ? "s" : "");
        } else {
            return FORMAT.format(seconds) + " second" + (seconds != 1 ? "s" : "");
        }
    }

    private static final long START_MILLIS = System.currentTimeMillis();

    private static String formatMs(double time) {
        time = time / 1000000D;
        return Math.round(time * 100.0) / 100.0 + "ms";
    }

    private static String format(double tps) {
        return ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

}