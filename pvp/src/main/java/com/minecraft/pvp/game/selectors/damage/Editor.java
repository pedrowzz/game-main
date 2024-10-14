package com.minecraft.pvp.game.selectors.damage;

import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.translation.Language;
import com.minecraft.pvp.game.types.Damage;
import com.minecraft.pvp.user.User;
import com.minecraft.pvp.util.DamageSettings;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

@Getter
@Setter
public class Editor implements BukkitInterface {

    private User owner;
    private Language language;

    public Editor(User user) {
        this.owner = user;
        this.language = user.getAccount().getLanguage();
    }

    public void build() {
        Inventory inventory = Bukkit.createInventory(null, 27, "Editor");

        inventory.setItem(10, new ItemFactory(Material.INK_SACK).setName(language.translate("pvp.damage.container.damage_types")).setDescription(language.translate("pvp.damage.container.damage_types_description")).setDurability(1).getStack());
        inventory.setItem(12, new ItemFactory(Material.WATCH).setName(language.translate("pvp.damage.container.damage_delays")).setDescription(language.translate("pvp.damage.container.damage_delays_description")).getStack());

        DamageSettings settings = this.owner.getDamageSettings();

        ItemFactory drops = new ItemFactory(Material.WOOD_SPADE).setName("§aDrops").addItemFlag(ItemFlag.values());

        if (settings.isDrops()) {
            drops.setDescription(language.translate("pvp.damage.container.drops.click_to_disable"));
            drops.glow();
        } else drops.setDescription(language.translate("pvp.damage.container.drops.click_to_enable"));

        ItemFactory wither = new ItemFactory(Material.SKULL_ITEM).setName("§aWither").addItemFlag(ItemFlag.values());

        if (settings.isWither()) {
            wither.setDescription(language.translate("pvp.damage.container.wither.click_to_disable"));
            wither.setDurability(1);
        } else {
            wither.setDescription(language.translate("pvp.damage.container.wither.click_to_enable"));
            wither.setDurability(0);
        }

        inventory.setItem(14, drops.getStack());
        inventory.setItem(16, wither.getStack());

        getOwner().getPlayer().openInventory(inventory);
    }

    public void buildDamagesType() {
        Inventory inventory = Bukkit.createInventory(null, 27, language.translate("container.damages_type_title"));

        int i = 11;

        for (Damage.DamageType damageType : Damage.DamageType.values()) {
            ItemFactory itemFactory = new ItemFactory(Material.STAINED_GLASS_PANE);

            itemFactory.setName(damageType.getName(getLanguage(), true));
            itemFactory.setDescription(damageType.getDescription());
            itemFactory.setDurability(damageType.getDurability());

            if (getOwner().getDamageSettings().getType() == damageType) {
                itemFactory.glow();
            }

            inventory.setItem(i, itemFactory.getStack());
            i++;
        }

        inventory.setItem(22, new ItemFactory(Material.ARROW).setName(language.translate("container.back_item_name")).setDescription(language.translate("container.back_item_description", "Editor")).getStack());

        getOwner().getPlayer().openInventory(inventory);
    }

    public void buildDamagesFrequency() {
        Inventory inventory = Bukkit.createInventory(null, 27, language.translate("container.damages_frequency_title"));

        String damage = getOwner().getDamageSettings().getType() == Damage.DamageType.VARIABLE ? "§c2.0 §4❤ §7- §c4.5 §4❤" : getOwner().getDamageSettings().getType().getDamage() / 2 + "§4❤";

        int i = 11;
        for (Damage.DamageFrequency frequency : Damage.DamageFrequency.values()) {

            ItemFactory itemFactory = new ItemFactory();

            itemFactory.setItemStack(frequency.getIcon());
            itemFactory.setName("§c" + damage + " §c/ " + frequency.getDescription());
            itemFactory.setDescription(frequency.getExplanation());
            itemFactory.addItemFlag(ItemFlag.values());

            if (getOwner().getDamageSettings().getFrequency() == frequency) {
                itemFactory.glow();
            }

            inventory.setItem(i, itemFactory.getStack());

            i++;
        }

        inventory.setItem(22, new ItemFactory(Material.ARROW).setName(language.translate("container.back_item_name")).setDescription(language.translate("container.back_item_description", "Editor")).getStack());

        getOwner().getPlayer().openInventory(inventory);
    }

}