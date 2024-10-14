package com.minecraft.pvp.util;

import com.minecraft.pvp.game.types.Damage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DamageSettings {

    private Damage.DamageType type = Damage.DamageType.EASY;
    private Damage.DamageFrequency frequency = Damage.DamageFrequency.THIRD;
    private boolean drops = false, wither = false;

    private Damage.DamageType challenge = Damage.DamageType.EASY;
    private boolean inChallenge = false;

}