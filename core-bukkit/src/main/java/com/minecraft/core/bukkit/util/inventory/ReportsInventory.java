package com.minecraft.core.bukkit.util.inventory;

import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.command.ReportsCommand;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.util.anticheat.report.Complaint;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportsInventory implements BukkitInterface {

    private final List<Integer> allowedSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

    protected final Player player;
    protected final Account account;
    protected final List<Complaint> complaints;

    protected Selector inventory;

    public ReportsInventory(final Player player, final Account account, final List<Complaint> complaints) {
        this.player = player;
        this.account = account;
        this.complaints = complaints;
        this.inventory = build();
    }

    private Selector build() {
        final Selector.Builder response = Selector.builder().withName("Reports").withAllowedSlots(allowedSlots).withSize(45).withNextPageSlot(44).withPreviousPageSlot(36);

        final List<ItemStack> itemStacks = new ArrayList<>();

        for (final Complaint complaint : this.complaints) {
            ItemFactory itemFactory = new ItemFactory(Material.SKULL_ITEM).setDurability(3).setSkull(complaint.getReported().getUsername());
            itemFactory.setName("§a" + complaint.getReported().getDisplayName());

            final List<String> description = new ArrayList<>();

            description.add("§8" + complaint.getServer());
            description.add(" ");
            description.add("§7Motivo: §f" + complaint.getReason());
            description.add("§7Quem enviou: §f" + complaint.getReporter().getDisplayName());
            description.add(" ");
            description.add("§7Expira em: §f" + ReportsCommand.format(ChronoUnit.SECONDS.between(LocalDateTime.now(), complaint.getExpiresAt())));
            description.add(" ");
            description.add("§eUtilize /go para ir!");

            itemFactory.setDescription(description);

            itemStacks.add(itemFactory.getStack());
        }

        response.withItems(itemStacks);

        return response.build();
    }

    public void openInventory() {
        inventory.open(player);
    }

}