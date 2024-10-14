package com.minecraft.core.bukkit.command;

import com.minecraft.core.bukkit.util.particle.ParticleBuilder;
import com.minecraft.core.bukkit.util.particle.ParticleEffect;
import com.minecraft.core.bukkit.util.particle.data.color.RegularColor;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.enums.Rank;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleCommand {

    @Command(name = "particle", platform = Platform.PLAYER, rank = Rank.ADMINISTRATOR)
    public void handleCommand(Context<Player> context, int RGM) {
//        EnumParticle enumParticle = EnumParticle.valueOf(particle.toUpperCase());
        Location location = context.getSender().getEyeLocation().add(context.getSender().getEyeLocation().getDirection().multiply(5));
        RegularColor regularColor = RegularColor.fromHSVHue(RGM);
        new ParticleBuilder(ParticleEffect.SPELL_MOB, location).setParticleData(regularColor).displayNearby();
    }
}
