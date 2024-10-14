package com.minecraft.arcade.pvp.kit.list;

import com.minecraft.arcade.pvp.event.user.LivingUserInteractEvent;
import com.minecraft.arcade.pvp.kit.Kit;
import com.minecraft.arcade.pvp.kit.object.KitCategory;
import com.minecraft.arcade.pvp.user.User;
import com.minecraft.arcade.pvp.util.GameMetadata;
import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.util.cooldown.CooldownProvider;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.bukkit.util.worldedit.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Archer extends Kit {

    protected final HashMap<UUID, Skill> skillHashMap = new HashMap<>();

    protected final Skill DEFAULT = Skill.FIRE;

    public Archer() {
        setIcon(Pattern.of(Material.BOW));
        setItems(new ItemFactory(Material.BOW).setName(ChatColor.GREEN + DEFAULT.getName()).setUnbreakable().getStack());
        setKitCategory(KitCategory.STRATEGY);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;

        final Arrow arrow = (Arrow) event.getDamager();

        if (!arrow.hasMetadata("skill"))
            return;

        final Skill skill = (Skill) arrow.getMetadata("skill").get(0).value();
        skill.getApplier().apply(event);

        if (event.getEntity() instanceof Player) {
            if (!(arrow.getShooter() instanceof Player))
                return;

            final Player shooter = (Player) arrow.getShooter();
            final User user = User.fetch(shooter.getUniqueId());

            if (isUser(user)) {
                if (shooter.getEntityId() != event.getEntity().getEntityId()) {
                    shooter.getInventory().addItem(new ItemStack(Material.ARROW));

                    if (Constants.RANDOM.nextBoolean())
                        shooter.getInventory().addItem(new ItemStack(Material.ARROW));
                }

                event.setDamage(event.getDamage() * 0.75);
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Arrow))
            return;

        final Arrow arrow = (Arrow) event.getEntity();

        if (!(arrow.getShooter() instanceof Player))
            return;

        final Player player = (Player) arrow.getShooter();

        if (!isItem(player.getItemInHand()))
            return;

        final User user = User.fetch(player.getUniqueId());

        if (!isUser(user))
            return;

        arrow.setMetadata("skill", new GameMetadata(skillHashMap.computeIfAbsent(player.getUniqueId(), uuid -> DEFAULT)));
    }

    @EventHandler
    public void onPlayerInteract(LivingUserInteractEvent event) {
        if (!LEFT_INTERACT.contains(event.getAction()))
            return;

        final User user = event.getUser();

        if (!isUser(user))
            return;

        final Player player = user.getPlayer();

        final ItemStack itemStack = player.getItemInHand();

        if (!isItem(itemStack))
            return;

        if (CooldownProvider.getGenericInstance().hasCooldown(player, "kit.archer.switch"))
            return;

        Skill skill = skillHashMap.computeIfAbsent(player.getUniqueId(), s -> DEFAULT);

        if (skill == Skill.KNOCKBACK)
            itemStack.removeEnchantment(Enchantment.ARROW_KNOCKBACK);

        skillHashMap.put(player.getUniqueId(), skill = skill.next());

        if (skill == Skill.KNOCKBACK)
            itemStack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);

        player.sendMessage("§aSkill alterada para " + skill.getName());

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + skill.getName());
        itemStack.setItemMeta(itemMeta);

        player.updateInventory();

        CooldownProvider.getGenericInstance().addCooldown(player.getUniqueId(), "kit.archer.switch", 0.5, false);
    }

    @AllArgsConstructor
    @Getter
    public enum Skill {

        FIRE(0, "Fogo", event -> {
            event.getEntity().setFireTicks(event.getEntity().getFireTicks() + 80);
        }),

        POISON(1, "Veneno", event -> {
            Player player = (Player) event.getEntity();
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 2));
        }),

        SLOWNESS(2, "Lentidão", event -> {
            Player player = (Player) event.getEntity();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2));
        }),

        KNOCKBACK(3, "Knockback", event -> {
            //Nothing
        }),

        DAMAGE(4, "Dano", event -> {
            event.setDamage(event.getDamage() + 2);
        });

        private int id;
        private final String name;
        private Applier applier;

        interface Applier {
            void apply(EntityDamageByEntityEvent event);
        }

        public Skill next() {
            if (this == DAMAGE)
                return FIRE;
            return fromId(this.id + 1);
        }

        public static Skill fromId(int id) {
            return Arrays.stream(values()).filter(c -> c.getId() == id).findFirst().orElse(null);
        }
    }

}