package com.minecraft.core.bukkit.command;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.account.datas.SkinData;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.disguise.PlayerDisguise;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.annotation.Completer;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.core.database.redis.Redis;
import com.minecraft.core.enums.Rank;
import com.mojang.authlib.properties.Property;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ForceskinCommand implements BukkitInterface {

    @Command(name = "forceskin", usage = "{label} <target> <skin>", rank = Rank.ADMINISTRATOR, platform = Platform.PLAYER)
    public final void handleCommand(final Context<Player> context, final Player target, final String skin) {
        final Player sender = context.getSender();

        if (target == null || !sender.canSee(target)) {
            context.info("target.not_found");
            return;
        }

        if (!Constants.isValid(skin)) {
            context.info("object.not_found", "Skin");
            return;
        }

        async(() -> {
            final UUID uniqueId = Constants.getMojangAPI().getUniqueId(skin);

            if (uniqueId == null) {
                context.info("object.not_found", "Skin");
                return;
            }

            final Property property = Constants.getMojangAPI().getProperty(uniqueId);

            if (property == null) {
                context.info("object.not_found", "Skin");
                return;
            }

            final Account account = Account.fetch(target.getUniqueId());

            if (account == null) {
                context.info("target.not_found");
                return;
            }

            sender.sendMessage("§eVocê alterou a skin de §6" + target.getName() + " §epara §b" + skin + "§e.");

            final SkinData skinData = account.getSkinData();

            skinData.setName(skin);
            skinData.setValue(property.getValue());
            skinData.setSignature(property.getSignature());
            skinData.setSource(SkinData.Source.FORCED);
            skinData.setUpdatedAt(System.currentTimeMillis());

            account.getData(Columns.SKIN).setData(skinData.toJson());
            account.getDataStorage().saveColumn(Columns.SKIN);

            Constants.getRedis().publish(Redis.SKIN_CHANGE_CHANNEL, account.getUniqueId() + ":" + property.getValue() + ":" + property.getSignature());

            sync(() -> PlayerDisguise.changeSkin(target, property));
        });
    }

    @Completer(name = "forceskin")
    public final List<String> handleComplete(final Context<CommandSender> context) {
        if (context.argsCount() == 1)
            return getOnlineNicknames(context);
        return Collections.emptyList();
    }

}