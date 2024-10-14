package com.minecraft.thebridge.util.bo3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public class BO3Block {

    protected int x, y, z;
    protected Material material;
    protected byte data;

}