package com.minecraft.hungergames.util.bo3;

import com.minecraft.core.bukkit.util.worldedit.Pattern;
import org.bukkit.Material;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BO3Reader {

    private final File file;

    public BO3Reader(File file) {
        this.file = file;
    }

    public BO3 read() {

        BO3 bo3 = new BO3();

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (line.startsWith("#")) /* Ignoring file comments */
                    continue;

                if (line.startsWith("hide();")) {
                    bo3.setHidden(true);
                    continue;
                }

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
                    bo3.addBlock(new BO3Block(x, y, z, Pattern.of(material, data)));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new EmptyBO3();
        }
        return bo3;
    }
}
