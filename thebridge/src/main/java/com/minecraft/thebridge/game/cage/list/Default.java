package com.minecraft.thebridge.game.cage.list;

import com.minecraft.core.bukkit.util.worldedit.Pattern;
import com.minecraft.thebridge.TheBridge;
import com.minecraft.thebridge.game.cage.Cage;
import org.bukkit.Material;

public class Default extends Cage {

    public Default(TheBridge theBridge) {
        super(theBridge);

        setDisplayName("Padrão");
        setIcon(Pattern.of(Material.STAINED_GLASS));
    }

}