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
import com.minecraft.core.server.packet.ServerPayload;
import com.minecraft.core.translation.Language;
import com.minecraft.hub.Hub;
import com.minecraft.hub.user.User;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class LobbySelector implements InventoryProvider {

    protected final Hub hub;

    private final SmartInventory inventory = SmartInventory.builder()
            .id("lobby_selector")
            .provider(this)
            .size(3, 9)
            .title("lobby.lobbies_selector")
            .closeable(true)
            .build();

    public LobbySelector(final Hub hub) {
        this.hub = hub;
    }

    public void execute(Player player, InventoryContents inventoryContents) {

        Pagination pagination = inventoryContents.pagination();

        List<Server> serverList = Constants.getServerStorage().getServers(Constants.getServerType());
        ClickableItem[] items = new ClickableItem[serverList.size()];

        int position = 0;

        Language language = Account.fetch(player.getUniqueId()).getLanguage();
        for (Server server : serverList) {

            if (server.isDead())
                continue;

            ServerPayload payload = server.getBreath();

            if (!payload.isWritten("id"))
                continue;

            int id = Integer.parseInt(String.valueOf(payload.get("id")));

            ItemFactory itemFactory = new ItemFactory(Material.STAINED_GLASS_PANE);
            itemFactory.setDurability(Bukkit.getPort() == server.getPort() ? 5 : 7);
            itemFactory.setName("§a" + this.hub.getLobby().getMode().getName() + " #" + id);
            itemFactory.setDescription("§7Players: " + payload.getOnlinePlayers() + "/" + payload.getMaxPlayers() + "\n\n§e" + language.translate("information.click_to_connect"));

            items[position] = ClickableItem.of(itemFactory.getStack(), (clickEvent) -> {
                final Player whoClicked = (Player) clickEvent.getWhoClicked();

                whoClicked.closeInventory();

                final User user = User.fetch(whoClicked.getUniqueId());
                final Account account = user.getAccount();

                if (user.hasCooldown(CONNECTION_COOLDOWN_KEY)) {
                    whoClicked.sendMessage(account.getLanguage().translate("wait_to_connect", Constants.SIMPLE_DECIMAL_FORMAT.format(user.getCooldown(CONNECTION_COOLDOWN_KEY).getRemaining())));
                    return;
                }

                user.addCooldown(CONNECTION_COOLDOWN_KEY, 3);
                account.connect(server);
            });

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

}