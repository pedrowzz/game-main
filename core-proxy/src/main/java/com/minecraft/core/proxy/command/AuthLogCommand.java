package com.minecraft.core.proxy.command;

import com.minecraft.core.account.Account;
import com.minecraft.core.account.fields.Preference;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.core.enums.Rank;
import com.minecraft.core.proxy.util.command.ProxyInterface;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AuthLogCommand implements ProxyInterface {

    @Command(name = "acmode", rank = Rank.TRIAL_MODERATOR)
    public void handleCommand(Context<ProxiedPlayer> context) {
        Account account = context.getAccount();
        account.setPreference(Preference.ANTICHEAT, !account.getPreference(Preference.ANTICHEAT));
        account.getData(Columns.PREFERENCES).setData(account.getPreferences());
        boolean enabled = account.getPreference(Preference.ANTICHEAT);
        context.info("command.acmode.toggle", enabled);
        async(() -> account.getDataStorage().saveColumn(Columns.PREFERENCES));
    }
}
