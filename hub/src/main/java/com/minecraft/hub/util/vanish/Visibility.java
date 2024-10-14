package com.minecraft.hub.util.vanish;

import com.minecraft.core.account.Account;
import com.minecraft.core.account.fields.Preference;
import com.minecraft.hub.user.User;
import lombok.Getter;
import org.bukkit.entity.Player;

public class Visibility {

    @Getter
    public static final Visibility INSTANCE = new Visibility();

    public void refresh(final Player observer) {
        final User user = User.fetch(observer.getUniqueId());

        final Account observerAccount = user.getAccount();
        final Preference preference = Preference.LOBBY_PLAYER_VISIBILITY;

        for (final Player target : observer.getWorld().getPlayers()) {
            if (observer.getEntityId() == target.getEntityId())
                continue;
            if (!observerAccount.getPreference(preference))
                setVisibility(observer, target, false);
        }
    }

    protected void show(final Player observer, final Player target) {
        if (!observer.canSee(target)) {
            observer.showPlayer(target);
        }
    }

    protected void hide(final Player observer, final Player target) {
        if (observer.canSee(target)) {
            observer.hidePlayer(target);
        }
    }

    protected void setVisibility(final Player observer, final Player target, boolean state) {
        if (state) {
            show(observer, target);
        } else {
            hide(observer, target);
        }
    }

}