package com.minecraft.core.bukkit.anticheat.player;

import com.minecraft.core.bukkit.anticheat.modules.list.AutoClick;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class Suspect {

    private final Player player;


    private AutoClick.PlayerClickHistory playerClickHistory = new AutoClick.PlayerClickHistory(); // Auto Click

    public UUID getUniqueId() {
        return this.getPlayer().getUniqueId();
    }
}
