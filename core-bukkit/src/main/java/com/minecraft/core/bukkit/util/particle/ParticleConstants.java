package com.minecraft.core.bukkit.util.particle;

import com.minecraft.core.bukkit.util.particle.utils.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ParticleConstants {

    public static final Class ITEM_STACK_CLASS = ReflectionUtils.getNMSClass("ItemStack");

    public static final Class PACKET_CLASS = ReflectionUtils.getNMSClass("Packet");

    public static final Class PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS = ReflectionUtils.getNMSClass("PacketPlayOutWorldParticles");

    public static final Class PARTICLE_ENUM;

    public static final Class PARTICLE_CLASS;

    static {
        PARTICLE_ENUM = (ReflectionUtils.MINECRAFT_VERSION < 13) ? ReflectionUtils.getNMSClass("EnumParticle") : null;
        PARTICLE_CLASS = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getNMSClass("Particle");
    }

    public static final Class MINECRAFT_KEY_CLASS = ReflectionUtils.getNMSClass("MinecraftKey");

    public static final Class REGISTRY_CLASS;

    static {
        REGISTRY_CLASS = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getNMSClass("IRegistry");
    }

    public static final Class BLOCK_CLASS = ReflectionUtils.getNMSClass("Block");

    public static final Class BLOCK_DATA_INTERFACE = ReflectionUtils.getNMSClass("IBlockData");

    public static final Class BLOCKS_CLASS;

    static {
        BLOCKS_CLASS = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getNMSClass("Blocks");
    }

    public static final Class ENTITY_PLAYER_CLASS = ReflectionUtils.getNMSClass("EntityPlayer");

    public static final Class PLAYER_CONNECTION_CLASS = ReflectionUtils.getNMSClass("PlayerConnection");

    public static final Class CRAFT_PLAYER_CLASS = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");

    public static final Class CRAFT_ITEM_STACK_CLASS = ReflectionUtils.getCraftBukkitClass("inventory.CraftItemStack");

    public static final Class PARTICLE_PARAM_CLASS;

    public static final Class PARTICLE_PARAM_REDSTONE_CLASS;

    public static final Class PARTICLE_PARAM_BLOCK_CLASS;

    public static final Class PARTICLE_PARAM_ITEM_CLASS;

    public static final Method REGISTRY_GET_METHOD;

    static {
        PARTICLE_PARAM_CLASS = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getNMSClass("ParticleParam");
        PARTICLE_PARAM_REDSTONE_CLASS = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getNMSClass("ParticleParamRedstone");
        PARTICLE_PARAM_BLOCK_CLASS = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getNMSClass("ParticleParamBlock");
        PARTICLE_PARAM_ITEM_CLASS = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getNMSClass("ParticleParamItem");
        REGISTRY_GET_METHOD = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getMethodOrNull(REGISTRY_CLASS, "get", new Class[]{MINECRAFT_KEY_CLASS});
    }

    public static final Method PLAYER_CONNECTION_SEND_PACKET_METHOD = ReflectionUtils.getMethodOrNull(PLAYER_CONNECTION_CLASS, "sendPacket", new Class[]{PACKET_CLASS});

    public static final Method CRAFT_PLAYER_GET_HANDLE_METHOD = ReflectionUtils.getMethodOrNull(CRAFT_PLAYER_CLASS, "getHandle", new Class[0]);

    public static final Method BLOCK_GET_BLOCK_DATA_METHOD = ReflectionUtils.getMethodOrNull(BLOCK_CLASS, "getBlockData", new Class[0]);

    public static final Method CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD = ReflectionUtils.getMethodOrNull(CRAFT_ITEM_STACK_CLASS, "asNMSCopy", new Class[]{ItemStack.class});

    public static final Field ENTITY_PLAYER_PLAYER_CONNECTION_FIELD = ReflectionUtils.getFieldOrNull(ENTITY_PLAYER_CLASS, "playerConnection", false);

    public static final Constructor PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR;

    static {
        if (ReflectionUtils.MINECRAFT_VERSION < 13) {
            PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, PARTICLE_ENUM, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class,
                    int[].class);
        } else if (ReflectionUtils.MINECRAFT_VERSION < 15) {
            PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, PARTICLE_PARAM_CLASS, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class);
        } else {
            PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, PARTICLE_PARAM_CLASS, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class);
        }
    }

    public static final Constructor MINECRAFT_KEY_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(MINECRAFT_KEY_CLASS, String.class);

    public static final Constructor PARTICLE_PARAM_REDSTONE_CONSTRUCTOR;

    public static final Constructor PARTICLE_PARAM_BLOCK_CONSTRUCTOR;

    public static final Constructor PARTICLE_PARAM_ITEM_CONSTRUCTOR;

    public static final Object PARTICLE_TYPE_REGISTRY;

    static {
        PARTICLE_PARAM_REDSTONE_CONSTRUCTOR = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_REDSTONE_CLASS, float.class, float.class, float.class, float.class);
        PARTICLE_PARAM_BLOCK_CONSTRUCTOR = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_BLOCK_CLASS, PARTICLE_CLASS, BLOCK_DATA_INTERFACE);
        PARTICLE_PARAM_ITEM_CONSTRUCTOR = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_ITEM_CLASS, PARTICLE_CLASS, ITEM_STACK_CLASS);
        PARTICLE_TYPE_REGISTRY = (ReflectionUtils.MINECRAFT_VERSION < 13) ? null : ReflectionUtils.readField(ReflectionUtils.getFieldOrNull(REGISTRY_CLASS, "PARTICLE_TYPE", false), null);
    }
}
