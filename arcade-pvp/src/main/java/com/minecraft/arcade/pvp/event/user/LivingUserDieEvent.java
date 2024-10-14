package com.minecraft.arcade.pvp.event.user;

import com.minecraft.arcade.pvp.user.User;
import com.minecraft.core.bukkit.event.handler.ServerEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@AllArgsConstructor
public class LivingUserDieEvent extends ServerEvent {

    private final User killed, killer;
    private final DieCause dieCause;
    private final List<ItemStack> drops;

    public boolean hasKiller() {
        return killer != null;
    }

    public enum DieCause {
        LOGOUT, KILL;
    }

}