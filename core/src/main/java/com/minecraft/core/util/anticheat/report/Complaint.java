package com.minecraft.core.util.anticheat.report;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Complaint {

    private String id;
    private Account reporter, reported;
    private String reason, server;
    private LocalDateTime expiresAt;

    public static String getUniqueId(Account account) {
        return "report:" + account.getUniqueId();
    }

    public static String getUniqueId(Account account, Account target) {
        return "report:" + account.getUniqueId() + "-" + target.getUniqueId() + "-" + System.currentTimeMillis();
    }

    public String toJson() {
        return Constants.GSON.toJson(this);
    }

}