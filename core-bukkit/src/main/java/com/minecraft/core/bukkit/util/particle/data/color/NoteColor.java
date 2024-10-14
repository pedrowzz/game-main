package com.minecraft.core.bukkit.util.particle.data.color;

import com.minecraft.core.bukkit.util.particle.ParticleEffect;
import com.minecraft.core.bukkit.util.particle.utils.MathUtils;

public final class NoteColor extends ParticleColor {

    public NoteColor(int note) {
        super(MathUtils.getMaxOrMin(note, 24, 0), 0, 0);
        setEffect(ParticleEffect.NOTE);
    }

    public void setEffect(ParticleEffect effect) {
        super.setEffect(ParticleEffect.NOTE);
    }

    public float getRed() {
        return super.getRed() / 24.0F;
    }

    public float getGreen() {
        return 0.0F;
    }

    public float getBlue() {
        return 0.0F;
    }

    public Object toNMSData() {
        return null;
    }

    public static NoteColor random() {
        return new NoteColor(MathUtils.generateRandomInteger(0, 24));
    }
}