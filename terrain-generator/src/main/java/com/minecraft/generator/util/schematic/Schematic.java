package com.minecraft.generator.util.schematic;

import com.minecraft.generator.util.schematic.data.*;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class Schematic {

    private final short[] blocks;
    private final byte[] blocksData;

    private final short width;
    private final short length;
    private final short height;
    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;

    public Schematic(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public Schematic(InputStream stream) {
        try {
            NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream));

            NamedTag rootTag = nbtStream.readNamedTag();
            nbtStream.close();
            if (!rootTag.getName().equals("Schematic")) {
                throw new Exception("Tag \"Schematic\" does not exist or is not first");
            }

            CompoundTag schematicTag = (CompoundTag) rootTag.getTag();

            Map<String, Tag> schematic = schematicTag.getValue();
            if (!schematic.containsKey("Blocks")) {
                throw new Exception("Schematic file is missing a \"Blocks\" tag");
            }

            this.width = getChildTag(schematic, "Width", ShortTag.class).getValue();
            this.length = getChildTag(schematic, "Length", ShortTag.class).getValue();
            this.height = getChildTag(schematic, "Height", ShortTag.class).getValue();

            this.offsetX = getChildTag(schematic, "WEOffsetX", IntTag.class).getValue();
            this.offsetY = getChildTag(schematic, "WEOffsetY", IntTag.class).getValue();
            this.offsetZ = getChildTag(schematic, "WEOffsetZ", IntTag.class).getValue();

            String materials = getChildTag(schematic, "Materials", StringTag.class).getValue();
            if (!materials.equals("Alpha")) {
                throw new Exception("Schematic file is not an Alpha schematic");
            }

            byte[] blockId = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
            this.blocksData = getChildTag(schematic, "Data", ByteArrayTag.class).getValue();
            this.blocks = new short[blockId.length];

            byte[] addId = new byte[0];
            if (schematic.containsKey("AddBlocks")) {
                addId = getChildTag(schematic, "AddBlocks", ByteArrayTag.class).getValue();
            }

            for (int index = 0; index < blockId.length; index++) {
                if ((index >> 1) >= addId.length) {
                    this.blocks[index] = (short) (blockId[index] & 0xFF);
                } else {
                    if ((index & 1) == 0) {
                        this.blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blockId[index] & 0xFF));
                    } else {
                        this.blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF));
                    }
                }
            }
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public void paste(Location location, boolean noAir) {

        MinecraftServer dedicatedServer = MinecraftServer.getServer();

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    if (noAir && blocks[index] == 0)
                        continue;
                    if (!dedicatedServer.isRunning() || dedicatedServer.isStopped())
                        break;
                    Block block = location.clone().add(offsetX, offsetY, offsetZ).add(x, y, z).getBlock();
                    block.setTypeIdAndData(blocks[index], blocksData[index], false);
                }
            }
        }
    }

    private <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws Exception {
        if (!items.containsKey(key)) {
            throw new Exception("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new Exception(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
}
