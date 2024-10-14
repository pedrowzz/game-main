/*
 * MIT License
 *
 * Copyright (c) 2021 ByteZ1337
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.minecraft.core.bukkit.util.particle.data.texture;

import com.minecraft.core.bukkit.util.particle.ParticleConstants;
import com.minecraft.core.bukkit.util.particle.PropertyType;
import com.minecraft.core.bukkit.util.particle.utils.ReflectionUtils;
import org.bukkit.Material;

import java.lang.reflect.Field;

public final class BlockTexture extends ParticleTexture {

    public BlockTexture(Material material) {
        super(material, (byte) 0);
    }

    public BlockTexture(Material material, byte data) {
        super(material, data);
    }

    public Object toNMSData() {
        if (getMaterial() == null || !getMaterial().isBlock() || getEffect() == null || !getEffect().hasProperty(PropertyType.REQUIRES_BLOCK))
            return null;
        if (ReflectionUtils.MINECRAFT_VERSION < 13)
            return super.toNMSData();
        Object block = getBlockData(getMaterial());
        if (block == null)
            return null;
        try {
            return ParticleConstants.PARTICLE_PARAM_BLOCK_CONSTRUCTOR.newInstance(new Object[]{getEffect().getNMSObject(), block});
        } catch (Exception ex) {
            return null;
        }
    }

    public Object getBlockData(Material material) {
        try {
            Field blockField = ReflectionUtils.getFieldOrNull(ParticleConstants.BLOCKS_CLASS, material.name(), false);
            if (blockField == null)
                return null;
            Object block = ReflectionUtils.readField(blockField, null);
            return ParticleConstants.BLOCK_GET_BLOCK_DATA_METHOD.invoke(block, new Object[0]);
        } catch (Exception ex) {
            return null;
        }
    }
}