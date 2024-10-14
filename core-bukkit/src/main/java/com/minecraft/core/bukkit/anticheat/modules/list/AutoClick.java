package com.minecraft.core.bukkit.anticheat.modules.list;

import com.minecraft.core.bukkit.BukkitGame;
import com.minecraft.core.bukkit.anticheat.AntiCheat;
import com.minecraft.core.bukkit.anticheat.modules.Module;
import com.minecraft.core.bukkit.anticheat.player.Suspect;
import com.minecraft.core.bukkit.util.variable.object.Variable;
import com.minecraft.core.enums.Rank;
import com.minecraft.core.util.anticheat.AntiCheatAlert;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.imanity.imanityspigot.packet.PacketHandler;

import java.util.ArrayList;
import java.util.List;

public class AutoClick extends Module implements PacketHandler {

    public AutoClick(AntiCheat antiCheat) {
        super(antiCheat, "", 20, true);
        register();
        Bukkit.getScheduler().runTaskTimer(BukkitGame.getEngine(), () -> Bukkit.getOnlinePlayers().forEach(player -> check(getAntiCheat().getSuspect(player.getUniqueId()))), 20L, 20L);
        Bukkit.imanity().registerPacketHandler(BukkitGame.getEngine(), this);
    }

    @Variable(name = "anticheat.autoclick.max_clicks", permission = Rank.ADMINISTRATOR)
    public int limit = 23;

    @Override
    public boolean onReceived(Player player, Object packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            MinecraftServer.getServer().postToMainThread(() -> process(player));
        }
        return true;
    }

    private void process(Player player) {

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        float pitch = entityPlayer.pitch;
        float yaw = entityPlayer.yaw;
        double locX = entityPlayer.locX;
        double locY = entityPlayer.locY + (double) entityPlayer.getHeadHeight();
        double locZ = entityPlayer.locZ;
        Vec3D vec3d = new Vec3D(locX, locY, locZ);
        float f3 = (float) Math.cos(-yaw * 0.017453292F - 3.1415927F);
        float f4 = (float) Math.sin(-yaw * 0.017453292F - 3.1415927F);
        float f5 = (float) -Math.cos(-pitch * 0.017453292F);
        float f6 = (float) Math.sin(-pitch * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = entityPlayer.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.CREATIVE ? 5.0D : 4.5D;
        Vec3D vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);

        MovingObjectPosition movingobjectposition = entityPlayer.world.rayTrace(vec3d, vec3d1, false);

        if (movingobjectposition == null
                || movingobjectposition.type != MovingObjectPosition.EnumMovingObjectType.BLOCK) {

            Suspect suspect = getAntiCheat().getSuspect(player.getUniqueId());
            PlayerClickHistory history = suspect.getPlayerClickHistory();
            history.setCount(history.getCount() + 1);
        }
    }

    @Override
    public boolean check(Suspect suspect) {

        PlayerClickHistory history = suspect.getPlayerClickHistory();
        history.compute();

        if (history.size() > 1 && history.average() >= limit) {

            AntiCheatAlert autoClickAlert = new AntiCheatAlert("Macro", suspect.getUniqueId(), getMaximumAlerts());
            autoClickAlert.addInformation("clicks", history.average() + "");
            send(suspect.getPlayer(), autoClickAlert);

            history.clear();
            return true;
        } else if (history.size() > 5 && history.average() > 5 && history.average() == history.minimum() && history.average() == history.maximum()) {

            AntiCheatAlert autoClickAlert = new AntiCheatAlert("Autoclick", suspect.getUniqueId(), getMaximumAlerts());
            autoClickAlert.addInformation("clicks", history.minimum() + "/" + history.average() + "/" + history.maximum());
            send(suspect.getPlayer(), autoClickAlert);

            history.clear();
            return true;
        }

        return false;
    }

    @Getter
    @Setter
    public static class PlayerClickHistory {

        private final List<Integer> clickHistory = new ArrayList<>();
        private int count = 0;

        public void compute() {
            clickHistory.add(0, count);
            this.count = 0;

            if (clickHistory.size() > 10)
                clickHistory.remove(clickHistory.size() - 1);
        }

        public List<Integer> getClickHistory() {
            return clickHistory;
        }

        public int average() {
            return (int) Math.round(clickHistory.stream().mapToInt(i -> i).average().orElse(-1));
        }

        public int minimum() {
            return clickHistory.stream().min(Integer::compare).orElse(-1);
        }

        public int maximum() {
            return clickHistory.stream().max(Integer::compare).orElse(-1);
        }

        public int size() {
            return getClickHistory().size();
        }

        public void clear() {
            getClickHistory().clear();
        }
    }
}
