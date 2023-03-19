package com.beycraft.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CommonUtils {
    public static class ZipUtils {
        private static final int BUFFER_SIZE = 4096;

        public static void unzip(File zipFile, File destDir) throws IOException {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDir.getPath() + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        }

        /**
         * Extracts a zip entry (file entry)
         *
         * @param zipIn
         * @param filePath
         * @throws IOException
         */
        private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        }
    }

    public static class PlayerUtils{

        public static PlayerEntity getPlayerByName(String playerName, ServerWorld level) {
            PlayerEntity returnablePlayer = null;
            for (PlayerEntity player : level.players()) {
                if(player.getName().getString().equals(playerName))
                    returnablePlayer = player;
            }
            return returnablePlayer;
        }
    }
}
