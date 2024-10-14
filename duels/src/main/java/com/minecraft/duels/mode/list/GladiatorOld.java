package com.minecraft.duels.mode.list;

import com.minecraft.core.bukkit.server.duels.DuelType;
import com.minecraft.core.bukkit.util.item.ItemFactory;
import com.minecraft.core.bukkit.util.variable.VariableStorage;
import com.minecraft.core.bukkit.util.variable.object.Variable;
import com.minecraft.core.bukkit.util.worldedit.WorldEditAPI;
import com.minecraft.core.database.enums.Columns;
import com.minecraft.core.enums.Rank;
import com.minecraft.duels.mode.Mode;
import com.minecraft.duels.room.Room;
import com.minecraft.duels.util.enums.RoomStage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GladiatorOld extends Mode implements VariableStorage {

    @Variable(name = "duels.gladiator_old.timer.clear_delay", permission = Rank.ADMINISTRATOR)
    public int clearDelay = 240;

    @Variable(name = "duels.gladiator_old.timer.wither_delay", permission = Rank.ADMINISTRATOR)
    public int witherDelay = 180;

    public GladiatorOld() {
        super(15, DuelType.GLADIATOR_OLD_1V1, DuelType.GLADIATOR_OLD_2V2);
        setName("Gladiator Old");
        loadVariables();
        setWins(Columns.DUELS_GLADIATOR_WINS);
        setLoses(Columns.DUELS_GLADIATOR_LOSSES);
        setWinstreak(Columns.DUELS_GLADIATOR_WINSTREAK);
        setWinstreakRecord(Columns.DUELS_GLADIATOR_MAX_WINSTREAK);
        setGames(Columns.DUELS_GLADIATOR_GAMES);
        setRating(Columns.DUELS_GLADIATOR_OLD_RATING);
    }

    @Override
    public void start(Room room) {
        super.start(room);

        room.getAlivePlayers().forEach(user -> {
            Player player = user.getPlayer();
            PlayerInventory playerInventory = player.getInventory();

            ItemStack SOUP = new ItemStack(Material.MUSHROOM_SOUP);

            playerInventory.setItem(0, new ItemFactory(Material.STONE_SWORD).setUnbreakable().getStack());
            playerInventory.setItem(1, new ItemStack(Material.WOOD, 64));
            playerInventory.setItem(8, new ItemStack(Material.WOOD_STAIRS, 64));

            playerInventory.setItem(13, new ItemStack(Material.BOWL, 64));
            playerInventory.setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
            playerInventory.setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));

            playerInventory.setItem(17, new ItemStack(Material.WOOD_AXE));

            playerInventory.setItem(22, new ItemStack(Material.BOWL, 64));
            playerInventory.setItem(23, new ItemStack(Material.RED_MUSHROOM, 64));
            playerInventory.setItem(24, new ItemStack(Material.BROWN_MUSHROOM, 64));

            for (int i = 0; i < 26; i++)
                playerInventory.addItem(SOUP);

            player.updateInventory();
        });
    }

    @Override
    public void tick(Room room) {
        super.tick(room);

        if (room.getStage() == RoomStage.PLAYING) {

            int time = room.getTime();

            if (time % witherDelay == 0) {
                room.getAlivePlayers().forEach(c -> c.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 6), true));
            } else if (time % clearDelay == 0) {
                room.getRollback().forEach(block -> WorldEditAPI.getInstance().setBlock(block.getLocation(), Material.AIR, (byte) 0));
                room.getRollback().clear();
                room.getAlivePlayers().forEach(c -> c.getPlayer().removePotionEffect(PotionEffectType.WITHER));
            }
        }
    }
}