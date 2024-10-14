package com.minecraft.thebridge.util.bo3;

import org.bukkit.Material;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BO3Common {

    public static BO3Object parse(final File file) throws Exception {
        final List<BO3Block> blocks = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("Block(")) {
                    line = line.substring("Block(".length(), line.length() - 1);

                    String[] arr = line.split(",");
                    int x = Integer.parseInt(arr[0]);
                    int y = Integer.parseInt(arr[1]);
                    int z = Integer.parseInt(arr[2]);

                    String rawMats = arr[3];

                    Material material;
                    byte data = 0;

                    if (rawMats.contains(":")) {
                        String[] mats = arr[3].split(":");
                        material = Material.valueOf(mats[0]);
                        data = Byte.parseByte(mats[1]);
                    } else {
                        material = Material.valueOf(rawMats);
                    }

                    blocks.add(new BO3Block(x, y, z, material, data));
                }
            }
        }
        return new BO3Object(blocks);
    }

}