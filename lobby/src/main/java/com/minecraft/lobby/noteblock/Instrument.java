package com.minecraft.lobby.noteblock;

import org.bukkit.Sound;

public class Instrument {

    public static Sound getInstrument(byte instrument) {
        switch (instrument) {
            case 1:
                return Sound.valueOf("NOTE_BASS_GUITAR");
            case 2:
                return Sound.valueOf("NOTE_BASS_DRUM");
            case 3:
                return Sound.valueOf("NOTE_SNARE_DRUM");
            case 4:
                return Sound.valueOf("NOTE_STICKS");
            default:
                return Sound.valueOf("NOTE_PIANO");
        }
    }

    public static org.bukkit.Instrument getBukkitInstrument(byte instrument) {
        switch (instrument) {
            case 1:
                return org.bukkit.Instrument.BASS_GUITAR;
            case 2:
                return org.bukkit.Instrument.BASS_DRUM;
            case 3:
                return org.bukkit.Instrument.SNARE_DRUM;
            case 4:
                return org.bukkit.Instrument.STICKS;
            default:
                return org.bukkit.Instrument.PIANO;
        }
    }
}
