package com.minecraft.core.bukkit.anticheat.modules;

import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.BukkitGame;
import com.minecraft.core.bukkit.anticheat.AntiCheat;
import com.minecraft.core.bukkit.anticheat.player.Suspect;
import com.minecraft.core.bukkit.util.listener.DynamicListener;
import com.minecraft.core.bukkit.util.variable.VariableStorage;
import com.minecraft.core.util.anticheat.AntiCheatAlert;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.imanity.imanityspigot.movement.MovementHandler;
import org.imanity.imanityspigot.packet.wrappers.MovementPacketWrapper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

@Getter
@Setter
public abstract class Module extends DynamicListener implements VariableStorage, MovementHandler {

    private final AntiCheat antiCheat;
    private String name;
    private int maximumAlerts;
    private boolean bannable;

    public Module(AntiCheat antiCheat, String name, Integer maximumAlerts, Boolean bannable) {
        this.antiCheat = antiCheat;
        this.name = name;
        this.maximumAlerts = maximumAlerts;
        this.bannable = bannable;
        loadVariables();
        register();
        Bukkit.imanity().registerMovementHandler(BukkitGame.getEngine(), this);
    }

    public abstract boolean check(Suspect suspect);

    public void send(Player player, AntiCheatAlert alert) {
        String message = Constants.GSON.toJson(alert);

        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF(message);
            player.sendPluginMessage(BukkitGame.getEngine(), "AntiCheat", b.toByteArray());
            b.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Failed to send anticheat alert");
        }
    }

    @Override
    public void onUpdateLocation(Player player, Location location, Location location1, MovementPacketWrapper movementPacketWrapper) {
    }

    @Override
    public void onUpdateRotation(Player player, Location location, Location location1, MovementPacketWrapper movementPacketWrapper) {
    }
}
