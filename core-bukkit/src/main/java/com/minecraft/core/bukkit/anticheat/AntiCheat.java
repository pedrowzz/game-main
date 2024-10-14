package com.minecraft.core.bukkit.anticheat;

import com.minecraft.core.bukkit.anticheat.modules.Module;
import com.minecraft.core.bukkit.anticheat.player.Suspect;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.*;

@Getter
@Setter
public class AntiCheat {

    private final Map<UUID, Suspect> suspects = Collections.synchronizedMap(new HashMap<>());
    private final List<Module> modules = new ArrayList<>();

    @SafeVarargs
    public final AntiCheat enable(Class<? extends Module>... classes) {
        for (Class<?> clazz : classes) {
            try {
                Module module = (Module) clazz.getConstructor(AntiCheat.class).newInstance(this);
                modules.add(module);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public Suspect getSuspect(UUID uuid) {
        return suspects.computeIfAbsent(uuid, suspect -> new Suspect(Bukkit.getPlayer(uuid)));
    }

    public Suspect removeSuspect(UUID uuid) {
        return suspects.remove(uuid);
    }

    public Module getModule(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
