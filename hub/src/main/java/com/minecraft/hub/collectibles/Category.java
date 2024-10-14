package com.minecraft.hub.collectibles;

import com.minecraft.core.bukkit.util.worldedit.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum Category {

    HATS(Pattern.of(Material.DIAMOND_HELMET));

    private final Pattern pattern;

    @Getter
    private static final Category[] values;

    static {
        values = values();
    }

}