package com.minecraft.core.bukkit.arcade.map.rollback;

import com.minecraft.core.bukkit.util.worldedit.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.block.Block;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class RollbackBlock {

    private final Block block;
    private Pattern pattern;
    private RollbackType type;

    public enum RollbackType {

        REMOVE_BLOCK,
        PLACE_BLOCK

    }

    @Override
    public String toString() {
        return "RollbackBlock{" +
                "block=" + block +
                ", pattern=" + pattern +
                ", type=" + type +
                '}';
    }
}
