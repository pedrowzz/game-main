/*
 * Copyright (C) YoloMC, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

package com.minecraft.core.bukkit.redis;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.event.player.PlayerUpdateTablistEvent;
import com.minecraft.core.bukkit.event.server.RedisPubSubEvent;
import com.minecraft.core.database.redis.Redis;
import com.minecraft.core.enums.PrefixType;
import com.minecraft.core.enums.Rank;
import com.minecraft.core.util.communication.AccountRankUpdateData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

public class BukkitRedisPubSub extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {

        if (channel.equals(Redis.RANK_UPDATE_CHANNEL)) {

            AccountRankUpdateData data = Constants.GSON.fromJson(message, AccountRankUpdateData.class);

            Account account = Account.fetch(data.getUniqueId());

            if (account == null)
                return;

            if (data.getAction() == AccountRankUpdateData.Action.REMOVE) {
                account.removeRank(data.getRank());
            } else if (data.getAction() == AccountRankUpdateData.Action.ADD) {
                account.giveRank(data.getRank(), data.getExpiration(), data.getAuthor());
            } else {
                account.removeRank(data.getRank());
                account.giveRank(data.getRank(), data.getExpiration(), data.getAuthor(), data.getAddedAt(), data.getUpdatedAt());
            }

            Player player = Bukkit.getPlayer(account.getUniqueId());

            if (player != null)
                player.setOp(account.hasPermission(Rank.TRIAL_MODERATOR));

            account.loadRanks();
            account.getTagList().loadTags();

            account.setProperty("account_tag", account.getTagList().getHighestTag());

            if (player != null)
                Bukkit.getPluginManager().callEvent(new PlayerUpdateTablistEvent(account, account.getTagList().getHighestTag(), account.getProperty("account_prefix_type", PrefixType.DEFAULT).getAs(PrefixType.class)));
        } else if (channel.equals(Redis.FLAG_UPDATE_CHANNEL)) {
            String[] args = message.split(":");

            Account account = Account.fetch(UUID.fromString(args[0]));

            if (account == null)
                return;

            account.setFlags(Integer.parseInt(args[1]));
        } else {
            Bukkit.getPluginManager().callEvent(new RedisPubSubEvent(channel, message));
        }
    }
}