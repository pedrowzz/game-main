package com.minecraft.arcade.duels.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minecraft.arcade.duels.Duels;
import com.minecraft.arcade.duels.room.Arena;
import com.minecraft.arcade.duels.user.User;
import com.minecraft.arcade.duels.user.kits.Kit;
import com.minecraft.arcade.duels.util.generator.VoidGenerator;
import com.minecraft.arcade.duels.util.world.WorldUtil;
import com.minecraft.core.Constants;
import com.minecraft.core.bukkit.arcade.ArcadeGame;
import com.minecraft.core.bukkit.arcade.game.GameType;
import com.minecraft.core.bukkit.arcade.map.Map;
import com.minecraft.core.bukkit.arcade.map.SignedLocation;
import com.minecraft.core.bukkit.arcade.map.area.Cuboid;
import com.minecraft.core.bukkit.arcade.map.synthetic.SyntheticLocation;
import com.minecraft.core.bukkit.util.file.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.metadata.FixedMetadataValue;
import org.imanity.imanityspigot.chunk.AsyncPriority;

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class Game extends ArcadeGame<Duels> {

    private final Set<Kit> kits = new HashSet<>();

    private static final File MAPS_DIRECTORY = new File(System.getProperty("user.home") + File.separator + "misc" + File.separator + "arcade" + File.separator + "duels");

    public Game(Duels plugin, Integer minRooms, Integer maxRooms, GameType type, String mapDirectory) {
        super(plugin, minRooms, maxRooms, mapDirectory, type);
    }

    @Override
    public boolean load() {

        System.out.println("Loading " + getName());

        if (!loadMaps())
            return false;

        int mapId = 0;

        for (int i = 0; i < getMinRooms(); i++) {

            if (mapId == getMaps().size())
                mapId = 0;

            Map map = getMap(mapId);

            mapId++;

            if (newArena(map) == null)
                return false;
        }

        return true;
    }

    @Override
    public void unload() {
        System.out.println("Unloading " + getName());
        getRooms().forEach(room -> FileUtil.delete(room.getWorld().getWorldFolder()));
        getRooms().clear();
    }

    @Override
    public Arena newArena(Map map) {

        int identifier = getAtomicInteger().incrementAndGet();
        File destination = new File(Bukkit.getWorldContainer(), "22pf" + identifier);

        if (destination.exists())
            FileUtil.delete(destination);

        if (!destination.mkdir()) {
            getAtomicInteger().decrementAndGet();
            return null;
        }

        if (!map.copyTo(destination))
            return null;

        WorldCreator creator = new WorldCreator(destination.getName());
        creator.generateStructures(false);
        creator.generator(VoidGenerator.getInstance());

        World world = Bukkit.createWorld(creator);
        WorldUtil.adjust(world, map.getArea().getChunks(world), AsyncPriority.BLOCKING_MAIN);

        Arena arena = new Arena(identifier, world, map, this);
        getRooms().add(arena);

        world.setMetadata("arena", new FixedMetadataValue(Duels.getInstance(), arena));

        System.out.println("Arena " + arena.getId() + " created.");
        return arena;
    }

    private boolean loadMaps() {
        File mapsDirectory = new File(MAPS_DIRECTORY, getName().toLowerCase());

        if (!mapsDirectory.isDirectory())
            return false;

        File[] files = mapsDirectory.listFiles();

        if (files == null || files.length == 0)
            return false;

        for (int i = 0; i < files.length; i++) {

            File source = files[i];

            System.out.println("[" + (i + 1) + "/" + files.length + "] Loading map " + source.getName() + ".");

            try {
                File configuration = new File(source, "config.json");

                if (!configuration.exists()) {
                    System.out.println("Configuration not found.");
                    continue;
                }

                JsonObject jsonObject = Constants.JSON_PARSER.parse(new FileReader(configuration)).getAsJsonObject();

                String name = jsonObject.get("name").getAsString();
                int buildLimit = jsonObject.get("build_limit").getAsInt();

                JsonArray jsonElements = jsonObject.get("locations").getAsJsonArray();

                Map map = new Map(i, name, getType(), source, jsonObject, buildLimit);

                for (JsonElement jsonElement : jsonElements) {
                    JsonObject locationObject = jsonElement.getAsJsonObject();

                    String locationName = locationObject.get("name").getAsString();
                    double posX = locationObject.get("x").getAsDouble();
                    double posY = locationObject.get("y").getAsDouble();
                    double posZ = locationObject.get("z").getAsDouble();
                    float yaw = locationObject.has("yaw") ? locationObject.get("yaw").getAsFloat() : 0;
                    float pitch = locationObject.has("pitch") ? locationObject.get("pitch").getAsFloat() : 0;

                    SignedLocation signedLocation = new SignedLocation(locationName, new SyntheticLocation(posX, posY, posZ, yaw, pitch));
                    map.getSignedLocations().add(signedLocation);
                }

                SignedLocation position1 = map.getLocation("map_limit_pos1");
                SignedLocation position2 = map.getLocation("map_limit_pos2");

                if (position1 == null || position2 == null)
                    continue;

                map.setArea(new Cuboid(position1.getLocation(), position2.getLocation()));

                System.out.println("[" + (map.getId() + 1) + "/" + files.length + "] Map " + map.getName() + " loaded.");
                getMaps().add(map);

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void operateScoreboard(User user) {
    }
}