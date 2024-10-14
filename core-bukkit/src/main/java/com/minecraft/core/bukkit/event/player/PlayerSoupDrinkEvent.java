package com.minecraft.core.bukkit.event.player;

import com.minecraft.core.bukkit.event.handler.ServerEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
@Setter
public class PlayerSoupDrinkEvent extends ServerEvent {

    private final Player player;
    private ItemStack itemStack;

}