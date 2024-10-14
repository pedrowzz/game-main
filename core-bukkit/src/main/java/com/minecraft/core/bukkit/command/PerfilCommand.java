package com.minecraft.core.bukkit.command;

import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.util.inventory.PreferencesInventory;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.bukkit.util.selector.ClickableItem;
import com.minecraft.core.bukkit.util.selector.SmartInventory;
import com.minecraft.core.bukkit.util.selector.content.InventoryContents;
import com.minecraft.core.bukkit.util.selector.content.InventoryProvider;
import com.minecraft.core.command.annotation.Command;
import com.minecraft.core.command.command.Context;
import com.minecraft.core.command.platform.Platform;
import com.minecraft.core.enums.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

public class PerfilCommand implements InventoryProvider {

    private final SmartInventory inventory = SmartInventory.builder()
            .id("profile")
            .provider(this)
            .size(5, 9)
            .title("lobby.profile_item")
            .closeable(true)
            .build();

    public void execute(Player player, InventoryContents inventoryContents) {
        Account account = Account.fetch(player.getUniqueId());

        inventoryContents.set(2, 5, ClickableItem.empty(new ItemStack(Material.SKULL_ITEM)));

        inventoryContents.set(4, 3, ClickableItem.of(new ItemStack(Material.PAPER), event -> {
            StatisticsCommand.Statistics statistics = new StatisticsCommand.Statistics(player, account);
            CompletableFuture.runAsync(statistics::load).thenRun(statistics::open);
        }));

        inventoryContents.set(4, 4, ClickableItem.of(new ItemStack(Material.NAME_TAG), event -> Bukkit.dispatchCommand(event.getWhoClicked(), "medal")));

        inventoryContents.set(4, 5, ClickableItem.of(new ItemFactory().setSkullURL("2e2cc42015e6678f8fd49ccc01fbf787f1ba2c32bcf559a015332fc5db50").setName("§aSelecionar idioma").setDescription("§7Altere seu idioma.").getStack(), event -> new LanguageCommand.LangMenu(player, account, true).open()));

        inventoryContents.set(4, 6, ClickableItem.of(new ItemFactory(Material.REDSTONE_COMPARATOR).setName("§aPreferências").setDescription("§7Altere suas preferências.").getStack(), event -> new PreferencesInventory(player, account, true).openInventory()));

        inventoryContents.set(4, 7, ClickableItem.empty(new ItemFactory(Material.ITEM_FRAME).setName("§aSkin").setDescription("§7Altere sua skin atual.").getStack()));
    }

    @Command(name = "testezudo", aliases = {"perfil"}, rank = Rank.DEVELOPER_ADMIN, platform = Platform.PLAYER)
    public void handleCommand(Context<Player> context) {
        inventory.open(context.getSender());
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        execute(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        execute(player, contents);
    }

}