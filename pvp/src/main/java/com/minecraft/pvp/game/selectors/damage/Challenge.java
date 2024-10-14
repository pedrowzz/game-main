package com.minecraft.pvp.game.selectors.damage;

import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.translation.Language;
import com.minecraft.pvp.game.types.Damage;
import com.minecraft.pvp.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Challenge implements BukkitInterface {

    private User owner;
    private Language language;

    public Challenge(User user) {
        this.owner = user;
        this.language = user.getAccount().getLanguage();
    }

    public void build() {
        Inventory inventory = Bukkit.createInventory(null, 27, language.translate("container.challenges_title"));

        int i = 10;
        for (Damage.DamageType damageType : Damage.DamageType.values()) {
            if (damageType == Damage.DamageType.VARIABLE) continue;

            ItemFactory itemFactory = new ItemFactory(Material.STAINED_CLAY);

            itemFactory.setName(damageType.getColor() + damageType.getName(getLanguage(), false));
            itemFactory.setDurability(damageType.getDurability());

            if (getOwner().getDamageSettings().getChallenge() == damageType) {
                itemFactory.glow();
            }

            List<String> description = new ArrayList<>();

            description.add("§7Seu recorde: §f" + format(owner.getAccount().getData(damageType.getColumns()).getAsInt()));
            description.add("");
            description.add("§eClique para selecionar!");

            itemFactory.setDescription(description);

            inventory.setItem(i, itemFactory.getStack());

            i += 2;
        }

        getOwner().getPlayer().openInventory(inventory);
    }

}