package com.beycraft.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.io.*;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CommonUtils {

    public static class AttributesUtil{
        public static void setAttribute(LivingEntity entity, String name, Attribute attribute, UUID uuid, double amount, AttributeModifier.Operation operation) {
            ModifiableAttributeInstance instance = entity.getAttribute(attribute);

            if (instance == null || entity.level.isClientSide) {
                return;
            }

            AttributeModifier modifier = instance.getModifier(uuid);

            if (amount == 0 || modifier != null && (modifier.getAmount() != amount || modifier.getOperation() != operation)) {
                instance.removeModifier(uuid);
                return;
            }

            modifier = instance.getModifier(uuid);

            if (modifier == null) {
                modifier = new AttributeModifier(uuid, name, amount, operation);
                instance.addTransientModifier(modifier);
            }
        }
    }

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
