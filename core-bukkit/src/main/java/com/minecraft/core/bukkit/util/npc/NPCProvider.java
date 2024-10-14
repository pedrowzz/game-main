package com.minecraft.core.bukkit.util.npc;

import com.minecraft.core.bukkit.BukkitGame;
import com.minecraft.core.bukkit.util.protocol.PacketInjector;
import com.minecraft.core.bukkit.util.protocol.PacketListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class NPCProvider {

    private final Set<NPC> npcs = new HashSet<>();
    private final PacketInjector packetInjector;
    private final NPCListener npcListener;

    public NPCProvider(BukkitGame plugin) {
        packetInjector = new PacketInjector();
        npcListener = new NPCListener();
        Bukkit.getServer().getPluginManager().registerEvents(npcListener, plugin);
        npcListener.setLoaded(true);
        PacketListener.setNPCListener(npcListener);
    }

    public void register(NPC npc) {
        this.npcs.add(npc);
    }

    public void unregister(NPC npc) {
        this.npcs.remove(npc);
    }

    public void remove(Player player) {
        this.npcs.removeIf(npc -> npc.getViewer().getEntityId() == player.getEntityId());
    }

    public List<NPC> getPlayerHumans(Player player) {
        return npcs.stream().filter(c -> c.getViewer().getEntityId() == player.getEntityId()).collect(Collectors.toList());
    }
}