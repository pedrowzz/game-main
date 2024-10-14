package com.minecraft.core.bukkit.command;

import com.google.common.primitives.Ints;
import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.inventory.ReportsInventory;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.database.redis.Redis;
import com.minecraft.core.enums.Rank;
import com.minecraft.core.util.anticheat.report.Complaint;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;

public class ReportsCommand implements BukkitInterface {

    @Command(name = "reports", rank = Rank.DEVELOPER_ADMIN, platform = Platform.PLAYER)
    public void handleCommand(Context<Player> context) {
        async(() -> {
            List<Complaint> reports = getReports();
            sync(() -> new ReportsInventory(context.getSender(), context.getAccount(), reports).openInventory());
        });
    }

    protected List<Complaint> getReports() {
        try (Jedis jedis = Constants.getRedis().getResource(Redis.SERVER_CACHE)) {
            return jedis.keys("report:*").stream().map(key -> Constants.GSON.fromJson(jedis.get(key), Complaint.class)).collect(Collectors.toList());
        }
    }

    public static String format(long l) {
        int i = Ints.checkedCast(l);
        int seconds = i % 60;
        return "" + i / 60 + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

}