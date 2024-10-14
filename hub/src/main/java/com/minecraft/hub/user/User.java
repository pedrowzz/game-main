package com.minecraft.hub.user;

import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.util.bossbar.Bossbar;
import com.minecraft.core.bukkit.util.cooldown.CooldownProvider;
import com.minecraft.core.bukkit.util.cooldown.type.Cooldown;
import com.minecraft.core.bukkit.util.scoreboard.GameScoreboard;
import com.minecraft.hub.Hub;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
public class User {

    private transient Player player;
    private transient Account account;

    private transient GameScoreboard scoreboard;
    private transient Bossbar bossbar;

    private transient Location lastLocation;

    public User(Account account) {
        this.account = account;
    }

    public UUID getUniqueId() {
        return getAccount().getUniqueId();
    }

    public String getName() {
        return getAccount().getDisplayName();
    }

    public boolean hasCooldown(final String key) {
        Cooldown cooldown = CooldownProvider.getGenericInstance().getCooldown(getUniqueId(), key);
        return cooldown != null && !cooldown.expired();
    }

    public Cooldown getCooldown(final String key) {
        return CooldownProvider.getGenericInstance().getCooldown(getUniqueId(), key);
    }

    public void addCooldown(final String key, final double duration) {
        CooldownProvider.getGenericInstance().addCooldown(getUniqueId(), key, key, duration, false);
    }

    public static User fetch(UUID uuid) {
        return Hub.getInstance().getUserStorage().getUser(uuid);
    }

    public User setAccount(Account account) {
        this.account = account;
        return this;
    }

}