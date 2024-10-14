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

package com.minecraft.core.bukkit.util.particle.data.color;

import com.minecraft.core.bukkit.util.particle.ParticleConstants;
import com.minecraft.core.bukkit.util.particle.ParticleEffect;
import com.minecraft.core.bukkit.util.particle.utils.MathUtils;
import com.minecraft.core.bukkit.util.particle.utils.ReflectionUtils;

import java.awt.*;

public class RegularColor extends ParticleColor {

    public RegularColor(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue());
    }

    public RegularColor(int red, int green, int blue) {
        super(MathUtils.getMaxOrMin(red, 255, 0), MathUtils.getMaxOrMin(green, 255, 0), MathUtils.getMaxOrMin(blue, 255, 0));
    }

    public float getRed() {
        return super.getRed() / 255.0F;
    }

    public float getGreen() {
        return super.getGreen() / 255.0F;
    }

    public float getBlue() {
        return super.getBlue() / 255.0F;
    }

    public Object toNMSData() {
        if (getEffect() != ParticleEffect.REDSTONE || ReflectionUtils.MINECRAFT_VERSION < 13)
            return new int[0];
        try {
            return ParticleConstants.PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(new Object[]{getRed(), getGreen(), getBlue(), 1.0F});
        } catch (Exception ex) {
            return null;
        }
    }

    public static RegularColor random() {
        return random(true);
    }

    public static RegularColor random(boolean highSaturation) {
        if (highSaturation)
            return fromHSVHue(MathUtils.generateRandomInteger(0, 360));
        return new RegularColor(new Color(MathUtils.RANDOM.nextInt(256), MathUtils.RANDOM.nextInt(256), MathUtils.RANDOM.nextInt(256)));
    }

    public static RegularColor fromHSVHue(int hue) {
        return new RegularColor(new Color(Color.HSBtoRGB(hue / 360.0F, 1.0F, 1.0F)));
    }
}