package com.minecraft.core.bukkit.util.particle.utils;

import com.minecraft.core.bukkit.util.particle.ParticleConstants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    private static final String NET_MINECRAFT_SERVER_PACKAGE_PATH;

    private static final String CRAFT_BUKKIT_PACKAGE_PATH;

    public static final int MINECRAFT_VERSION;

    static {
        String serverPath = Bukkit.getServer().getClass().getPackage().getName();
        String version = serverPath.substring(serverPath.lastIndexOf(".") + 1);
        NET_MINECRAFT_SERVER_PACKAGE_PATH = "net.minecraft.server." + version;
        CRAFT_BUKKIT_PACKAGE_PATH = "org.bukkit.craftbukkit." + version;
        String packageVersion = serverPath.substring(serverPath.lastIndexOf(".") + 2);
        MINECRAFT_VERSION = Integer.parseInt(packageVersion.substring(0, packageVersion.lastIndexOf("_")).replace("_", ".").substring(2));
    }

    public static String getNMSPath(String path) {
        return getNetMinecraftServerPackagePath() + "." + path;
    }

    public static Class<?> getNMSClass(String path) {
        try {
            return Class.forName(getNMSPath(path));
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getCraftBukkitPath(String path) {
        return getCraftBukkitPackagePath() + "." + path;
    }

    public static Class<?> getCraftBukkitClass(String path) {
        try {
            return Class.forName(getCraftBukkitPath(path));
        } catch (Exception ex) {
            return null;
        }
    }

    public static Method getMethodOrNull(Class targetClass, String methodName, Class<?>... parameterTypes) {
        try {
            return targetClass.getMethod(methodName, parameterTypes);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Field getFieldOrNull(Class targetClass, String fieldName, boolean declared) {
        try {
            return declared ? targetClass.getDeclaredField(fieldName) : targetClass.getField(fieldName);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Constructor getConstructorOrNull(Class targetClass, Class... parameterTypes) {
        try {
            return targetClass.getConstructor(parameterTypes);
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean existsClass(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static Object readField(Class targetClass, String fieldName, Object object) {
        if (targetClass == null || fieldName == null)
            return null;
        return readField(getFieldOrNull(targetClass, fieldName, false), object);
    }

    public static Object readField(Field field, Object object) {
        if (field == null)
            return null;
        try {
            return field.get(object);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Object readDeclaredField(Class targetClass, String fieldName, Object object) {
        if (targetClass == null || fieldName == null)
            return null;
        return readDeclaredField(getFieldOrNull(targetClass, fieldName, true), object);
    }

    public static Object readDeclaredField(Field field, Object object) {
        if (field == null)
            return null;
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (Exception ex) {
            return null;
        }
    }

    public static void writeDeclaredField(Class targetClass, String fieldName, Object object, Object value) {
        if (targetClass == null || fieldName == null)
            return;
        writeDeclaredField(getFieldOrNull(targetClass, fieldName, true), object, value);
    }

    public static void writeDeclaredField(Field field, Object object, Object value) {
        if (field == null)
            return;
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (Exception exception) {
        }
    }

    public static void writeField(Class targetClass, String fieldName, Object object, Object value) {
        if (targetClass == null || fieldName == null)
            return;
        writeField(getFieldOrNull(targetClass, fieldName, false), object, value);
    }

    public static void writeField(Field field, Object object, Object value) {
        if (field == null)
            return;
        try {
            field.set(object, value);
        } catch (Exception exception) {
        }
    }

    public static String getNetMinecraftServerPackagePath() {
        return NET_MINECRAFT_SERVER_PACKAGE_PATH;
    }

    public static String getCraftBukkitPackagePath() {
        return CRAFT_BUKKIT_PACKAGE_PATH;
    }

    public static Object getMinecraftKey(String key) {
        if (key == null)
            return null;
        try {
            return ParticleConstants.MINECRAFT_KEY_CONSTRUCTOR.newInstance(key);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Object getPlayerHandle(Player player) {
        if (player == null || player.getClass() != ParticleConstants.CRAFT_PLAYER_CLASS)
            return null;
        try {
            return ParticleConstants.CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Object getPlayerConnection(Player target) {
        try {
            return readField(ParticleConstants.ENTITY_PLAYER_PLAYER_CONNECTION_FIELD, getPlayerHandle(target));
        } catch (Exception ex) {
            return null;
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            ParticleConstants.PLAYER_CONNECTION_SEND_PACKET_METHOD.invoke(getPlayerConnection(player), packet);
        } catch (Exception exception) {
        }
    }
}
