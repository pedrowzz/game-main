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
import org.bukkit.inventory.ItemStack;

public final class ItemTexture extends ParticleTexture {
    private final ItemStack itemStack;

    public ItemTexture(ItemStack itemStack) {
        super((itemStack == null) ? null : itemStack.getType(), (byte) 0);
        this.itemStack = itemStack;
    }

    public Object toNMSData() {
        if (getMaterial() == null || getData() < 0 || getEffect() == null || !getEffect().hasProperty(PropertyType.REQUIRES_ITEM))
            return null;
        if (ReflectionUtils.MINECRAFT_VERSION < 13)
            return super.toNMSData();
        try {
            return ParticleConstants.PARTICLE_PARAM_ITEM_CONSTRUCTOR.newInstance(getEffect().getNMSObject(), toNMSItemStack(getItemStack()));
        } catch (Exception ex) {
            return null;
        }
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public static Object toNMSItemStack(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        try {
            return ParticleConstants.CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.invoke(null, itemStack);
        } catch (Exception ex) {
            return null;
        }
    }
}