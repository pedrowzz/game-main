package com.minecraft.thebridge.util.bo3;

import com.minecraft.core.bukkit.util.worldedit.WorldEditAPI;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

@RequiredArgsConstructor
public class BO3Object {

    @NonNull
    @Getter
    private List<BO3Block> blocks;

    public void paste(final Location location) {
        blocks.forEach(bo3Block -> WorldEditAPI.getInstance().setBlock(location.clone().add(bo3Block.getX(), bo3Block.getY(), bo3Block.getZ()), bo3Block.getMaterial(), bo3Block.getData()));
    }

    public void undo(final Location base) {
        blocks.forEach(bo3Block -> WorldEditAPI.getInstance().setBlock(base.clone().add(bo3Block.getX(), bo3Block.getY(), bo3Block.getZ()), Material.AIR, (byte) 0));
    }

}