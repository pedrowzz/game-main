package com.minecraft.arcade.pvp.kit.list;

import com.minecraft.arcade.pvp.event.user.LivingUserDieEvent;
import com.minecraft.arcade.pvp.kit.Kit;
import com.minecraft.arcade.pvp.kit.object.KitCategory;
import com.minecraft.core.bukkit.util.worldedit.Pattern;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Berserker extends Kit {

    public Berserker() {
        setIcon(Pattern.of(Material.SKULL_ITEM, 2));
        setKitCategory(KitCategory.COMBAT);
    }

    @Override
    public void appreciate(LivingUserDieEvent event) {

        Player player = event.getKiller().getPlayer();

        int time = 240;

        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            PotionEffect potionEffect = player.getActivePotionEffects().stream().filter(c -> c.getType() == PotionEffectType.INCREASE_DAMAGE).findAny().orElse(null);
            if (potionEffect != null)
                time += potionEffect.getDuration();
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, 0), true);
    }

}
