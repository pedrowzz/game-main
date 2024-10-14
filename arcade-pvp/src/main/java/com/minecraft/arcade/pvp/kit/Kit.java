package com.minecraft.arcade.pvp.kit;

import com.google.common.collect.ImmutableList;
import com.minecraft.arcade.pvp.PvP;
import com.minecraft.arcade.pvp.event.user.LivingUserDieEvent;
import com.minecraft.arcade.pvp.kit.object.CooldownType;
import com.minecraft.arcade.pvp.kit.object.KitCategory;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.arcade.pvp.util.Assistance;
import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.cooldown.CooldownProvider;
import com.minecraft.core.bukkit.util.cooldown.type.Cooldown;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.bukkit.util.variable.VariableStorage;
import com.minecraft.core.bukkit.util.worldedit.Pattern;
import com.minecraft.core.enums.Rank;
import com.minecraft.core.translation.Language;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Iterator;
import java.util.UUID;

@Setter
@Getter
public abstract class Kit implements Listener, BukkitInterface, VariableStorage, Assistance {

    private Rank rank = Rank.MEMBER;
    private KitCategory kitCategory = KitCategory.NONE;

    private boolean active = true;

    private int price = 10000;

    private double combatCooldown = 0;

    private final String name = getClass().getSimpleName();

    private String displayName;

    private Pattern icon = Pattern.of(Material.BARRIER);

    private long releasedAt = -1;

    private ImmutableList<ItemStack> itemStacks;

    private final PvP instance = PvP.getInstance();
    private final CooldownProvider cooldownProvider = CooldownProvider.getGenericInstance();

    protected final String cooldownKey, combatKey;
    protected final String portugueseDescription, englishDescription;

    public Kit() {
        this.displayName = getName();
        this.portugueseDescription = Language.PORTUGUESE.translate("kit." + this.name.toLowerCase() + ".description");
        this.englishDescription = Language.ENGLISH.translate("kit." + this.name.toLowerCase() + ".description");
        this.cooldownKey = "kit.cooldown." + this.name.toLowerCase();
        this.combatKey = "kit.combat." + this.name.toLowerCase();
    }

    public void dispatchCooldown(Player player) {
        Cooldown cooldown = cooldownProvider.getCooldown(player.getUniqueId(), cooldownKey);
        if (cooldown == null)
            cooldown = cooldownProvider.getCooldown(player.getUniqueId(), combatKey);
        if (!cooldown.expired())
            player.sendMessage(Account.fetch(player.getUniqueId()).getLanguage().translate("wait_to_use_kit", Constants.SIMPLE_DECIMAL_FORMAT.format(cooldown.getRemaining())));
    }

    public boolean isCooldown(Player player) {
        Cooldown cooldown = cooldownProvider.getCooldown(player.getUniqueId(), cooldownKey);
        return cooldown != null && !cooldown.expired();
    }

    public boolean isCombat(Player player) {
        Cooldown cooldown = cooldownProvider.getCooldown(player.getUniqueId(), combatKey);
        return cooldown != null && !cooldown.expired();
    }

    public void addCooldown(UUID uuid, CooldownType cooldownType, long duration) {
        cooldownProvider.addCooldown(uuid, cooldownType.getWord() + getName(), cooldownType == CooldownType.DEFAULT ? cooldownKey : combatKey, duration, true);
    }

    public void addCooldown(UUID uuid, CooldownType cooldownType, double duration) {
        cooldownProvider.addCooldown(uuid, cooldownType.getWord() + getDisplayName(), cooldownType == CooldownType.DEFAULT ? cooldownKey : combatKey, duration, true);
    }

    public void removeCooldown(Player player) {
        cooldownProvider.removeCooldown(player, cooldownKey);
    }

    public boolean isUser(final User user) {
        return user.getGame().getId() == 1 && user.getKitList().stream().anyMatch(c -> c == this);
    }

    public String getDescription(Language language) {
        return language == Language.PORTUGUESE ? portugueseDescription : englishDescription;
    }

    public boolean isUnique() {
        return this.releasedAt != -1;
    }

    protected void setItems(ItemStack... kitItems) {
        ImmutableList.Builder<ItemStack> listBuilder = ImmutableList.builder();
        for (ItemStack kitItem : kitItems) {
            if (kitItem.hasItemMeta()) {
                listBuilder.add(addTag(new ItemFactory(kitItem).setDescription("§7Kit " + getDisplayName()).getStack(), "kit." + getName().toLowerCase(), "kit", "undroppable"));
            } else {
                listBuilder.add(kitItem);
            }
        }
        this.itemStacks = listBuilder.build();
    }

    public void giveItems(final PlayerInventory playerInventory) {
        if (this.itemStacks == null)
            return;
        for (Iterator<ItemStack> it = this.itemStacks.stream().iterator(); it.hasNext(); ) {
            playerInventory.addItem(it.next());
        }
    }

    protected boolean isItem(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsStack != null && nmsStack.hasTag() && nmsStack.getTag().hasKey("kit." + getName().toLowerCase());
    }

    protected boolean isItem(ItemStack itemStack, Material material) {
        if (itemStack == null)
            return false;
        if (itemStack.getType() != material)
            return false;
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsStack != null && nmsStack.hasTag() && nmsStack.getTag().hasKey("kit." + getName().toLowerCase());
    }

    public void appreciate(LivingUserDieEvent event) {
    }

    public void resetAttributes(User user) {
    }

    public boolean isCombatCooldown() {
        return this.combatCooldown > 0;
    }

    public boolean isNone() {
        return this == PvP.getInstance().getKitStorage().getDefaultKit();
    }

}