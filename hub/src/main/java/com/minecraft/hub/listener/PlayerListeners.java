package com.minecraft.hub.listener;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.account.fields.Preference;
import com.minecraft.core.bukkit.command.ProfileCommand;
import com.minecraft.core.bukkit.event.player.PlayerShowEvent;
import com.minecraft.core.bukkit.event.player.PlayerTeamAssignEvent;
import com.minecraft.core.bukkit.util.BukkitInterface;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.clan.Clan;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.hub.Hub;
import com.minecraft.hub.user.User;
import com.minecraft.hub.user.storage.UserStorage;
import com.minecraft.hub.util.vanish.Visibility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListeners implements Listener, BukkitInterface {

    private final UserStorage storage;

    public PlayerListeners(final Hub hub) {
        this.storage = hub.getUserStorage();
    }

    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent event) {
        if (!event.hasItem()) return;

        if (event.getAction() == Action.PHYSICAL) return;

        final Player player = event.getPlayer();

        final ItemStack itemStack = event.getItem();
        final ItemMeta itemMeta = itemStack.getItemMeta();

        final Account account = Account.fetch(player.getUniqueId());

        if (itemStack.getType() == Material.INK_SACK && itemMeta != null && itemMeta.getDisplayName().contains("Players")) {
            final User user = User.fetch(player.getUniqueId());

            if (user.hasCooldown("visibility")) {
                player.sendMessage(account.getLanguage().translate("wait_generic", Constants.SIMPLE_DECIMAL_FORMAT.format(user.getCooldown("visibility").getRemaining())));
                return;
            }

            user.addCooldown("visibility", 5);

            if (itemMeta.getDisplayName().contains("OFF")) {
                player.setItemInHand(new ItemFactory(Material.INK_SACK).setDurability(10).setName("§fPlayers: §aON").getStack());
                player.sendMessage(account.getLanguage().translate("lobby.players_enabled"));
                account.setPreference(Preference.LOBBY_PLAYER_VISIBILITY, true);
            } else if (itemMeta.getDisplayName().contains("ON")) {
                player.setItemInHand(new ItemFactory(Material.INK_SACK).setDurability(8).setName("§fPlayers: §cOFF").getStack());
                player.sendMessage(account.getLanguage().translate("lobby.players_disabled"));
                account.setPreference(Preference.LOBBY_PLAYER_VISIBILITY, false);
            }

            player.updateInventory();
            Visibility.INSTANCE.refresh(player);

            async(() -> account.getDataStorage().saveColumn(Columns.PREFERENCES));
        } else if (itemStack.getType() == Material.SKULL_ITEM) {
            new ProfileCommand.ProfileMenu(player, account).open();
        }
    }

    @EventHandler
    public void onPlayerShowEvent(final PlayerShowEvent event) {
        if (!Account.fetch(event.getReceiver().getUniqueId()).getPreference(Preference.LOBBY_PLAYER_VISIBILITY))
            event.setCancelled(true);
    }

    @EventHandler
    public void onTeamAssign(PlayerTeamAssignEvent event) {
        final Account account = event.getAccount();

        if (account.hasClan()) {
            final Clan clan = Constants.getClanService().fetch(account.getData(Columns.CLAN).getAsInt());

            if (clan == null)
                return;

            event.getTeam().setSuffix(" " + ChatColor.valueOf(clan.getColor()) + "[" + clan.getTag().toUpperCase() + "]");
        }
    }

    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {

        if (!storage.getUser(event.getPlayer().getUniqueId()).getAccount().getProperty("lobby.build", false).getAsBoolean())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBucketFill(final PlayerBucketFillEvent event) {
        if (!storage.getUser(event.getPlayer().getUniqueId()).getAccount().getProperty("lobby.build", false).getAsBoolean())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerEditBookEvent(final PlayerEditBookEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerEditBookEvent(final PlayerEggThrowEvent event) {
        event.setHatching(false);
    }

    @EventHandler
    public void onPlayerFishEvent(final PlayerFishEvent event) {
        event.setCancelled(false);
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(final PlayerItemConsumeEvent event) {
        event.setCancelled(false);
    }

    @EventHandler
    public void onPlayerItemDamageEvent(final PlayerItemDamageEvent event) {
        event.setCancelled(false);
    }

    @EventHandler
    public void onPlayerPickupArrowEvent(final PlayerPickupArrowEvent event) {
        event.setCancelled(false);
    }

    @EventHandler
    public void onPlayerArmorStandManipulateEvent(final PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBedEnterEvent(final PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPortalEvent(final PlayerPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerShearEntityEvent(final PlayerShearEntityEvent event) {
        event.setCancelled(true);
    }

}