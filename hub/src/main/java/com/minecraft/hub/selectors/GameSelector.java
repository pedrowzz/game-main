package com.minecraft.hub.selectors;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.bukkit.util.selector.ClickableItem;
import com.minecraft.core.bukkit.util.selector.SmartInventory;
import com.minecraft.core.bukkit.util.selector.content.InventoryContents;
import com.minecraft.core.bukkit.util.selector.content.InventoryProvider;
import com.minecraft.core.bukkit.util.selector.content.Pagination;
import com.minecraft.core.bukkit.util.selector.content.SlotIterator;
import com.minecraft.core.server.Server;
import com.minecraft.core.server.ServerCategory;
import com.minecraft.core.server.ServerType;
import com.minecraft.core.translation.Language;
import com.minecraft.hub.Hub;
import com.minecraft.hub.user.User;
import com.minecraft.hub.util.enums.Games;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

@Getter
public class GameSelector implements InventoryProvider {

    protected final Hub hub;

    public GameSelector(final Hub hub) {
        this.hub = hub;
    }

    private final SmartInventory inventory = SmartInventory.builder()
            .id("game_selector")
            .provider(this)
            .size(3, 9)
            .title("lobby.games_selector")
            .closeable(true)
            .build();

    public void execute(Player player, InventoryContents inventoryContents) {

        Pagination pagination = inventoryContents.pagination();
        ClickableItem[] items = new ClickableItem[Games.getValues().length];

        int position = 0;

        Language language = Account.fetch(player.getUniqueId()).getLanguage();

        for (final Games game : Games.getValues()) {
            ItemStack itemStack = new ItemFactory(game.getMaterial()).setName("§a" + game.getName()).setDescription(32, language.translate("information.playing", count(game.getServerTypes()))).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).getStack();
            ClickableItem clickableItem = ClickableItem.of(itemStack, event -> connect((Player) event.getWhoClicked(), game.getServerTypes()[0]));
            items[position] = clickableItem;
            position++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        execute(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        execute(player, contents);
    }

    final String CONNECTION_COOLDOWN_KEY = "connect-cooldown";

    protected void connect(final Player player, final ServerType serverType) {
        final User user = User.fetch(player.getUniqueId());
        final Account account = user.getAccount();

        if (user.hasCooldown(CONNECTION_COOLDOWN_KEY)) {
            player.sendMessage(account.getLanguage().translate("wait_to_connect", Constants.SIMPLE_DECIMAL_FORMAT.format(user.getCooldown(CONNECTION_COOLDOWN_KEY).getRemaining())));
            return;
        }

        user.addCooldown(CONNECTION_COOLDOWN_KEY, 3);

        Server server = ServerCategory.LOBBY.getServerFinder().getBestServer(serverType);

        if (server == null || server.isDead()) {
            player.sendMessage(account.getLanguage().translate("no_server_available", serverType.getName()));
            return;
        }

        player.closeInventory();
        account.connect(server);
    }

    public String count(ServerType... serverTypes) {
        int count = Constants.getServerStorage().count(serverTypes);

        if (count == -1)
            return "...";

        return String.valueOf(count);
    }

}