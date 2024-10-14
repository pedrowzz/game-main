package com.minecraft.core.bukkit.util.file;

import java.io.*;
import java.nio.file.Path;
import java.util.function.Predicate;

public final class FileUtil {

    public static void copy(final File source, final File destination, Predicate<Path> predicate) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();

            if (files == null) return;

            for (String file : files) {

                File newSource = new File(source, file);

                if (predicate != null && !predicate.test(newSource.toPath())) {
                    continue;
                }

                File newDestination = new File(destination, file);
                copy(newSource, newDestination, predicate);
            }
        } else {
            InputStream inputStream = new FileInputStream(source);
            OutputStream outputStream = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];

            int i;

            while ((i = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, i);
            }

            inputStream.close();
            outputStream.close();
        }
    }

    public static void delete(final File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null)
                return;
            for (File child : files) {
                delete(child);
            }
        }
        file.delete();
    }
}