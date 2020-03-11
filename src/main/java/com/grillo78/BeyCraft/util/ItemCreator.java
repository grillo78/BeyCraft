package com.grillo78.BeyCraft.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDiskFrame;
import com.grillo78.BeyCraft.items.ItemBeyDriver;
import com.grillo78.BeyCraft.items.ItemBeyFrame;
import com.grillo78.BeyCraft.items.ItemBeyLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

public class ItemCreator {

    public static void getItemsFromFolder() {
        File itemsFolder = new File(Minecraft.getInstance().gameDir, "BeyParts");
        File beycraftResourcePack = new File(Minecraft.getInstance().getFileResourcePacks(),
                "Beycraft Resource Pack");
        if (!beycraftResourcePack.exists()) {
            beycraftResourcePack.mkdir();
            File packMCMeta = new File(beycraftResourcePack, "pack.mcmeta");
            try {
                packMCMeta.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!itemsFolder.exists()) {
            itemsFolder.mkdir();
        }
        File[] itemsFiles = itemsFolder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }
        });
        BeyCraft.logger.info(itemsFiles.length + " items was found in the folder");
        FileReader fileReader;
        Properties properties = new Properties();

        for (int i = 0; i < itemsFiles.length; i++) {
            try {
                fileReader = new FileReader(itemsFiles[i]);
                properties.load(fileReader);
                if (properties.get("part").equals("layer")) {
                    new ItemBeyLayer(properties.getProperty("name"),
                            new Integer(properties.getProperty("rotationDirection")),
                            new Integer(properties.getProperty("attack")),
                            new Integer(properties.getProperty("defense")),
                            new Integer(properties.getProperty("weight")), new Integer(properties.getProperty("burst")),
                            null, null, BeyTypes.getByName(properties.getProperty("type")));
                } else if (properties.get("part").equals("disk")) {
                    new ItemBeyDisk(properties.getProperty("name"), new Integer(properties.getProperty("attack")),
                            new Integer(properties.getProperty("defense")),
                            new Integer(properties.getProperty("weight")), new Integer(properties.getProperty("burst")),
                            null, null, BeyTypes.getByName(properties.getProperty("type")), new Item.Properties());
                } else if (properties.get("part").equals("frame")) {
                    new ItemBeyFrame(properties.getProperty("name"));
                } else if (properties.get("part").equals("framedisk")) {
                    new ItemBeyDiskFrame(properties.getProperty("name"), new Integer(properties.getProperty("attack")),
                            new Integer(properties.getProperty("defense")),
                            new Integer(properties.getProperty("weight")), new Integer(properties.getProperty("burst")),
                            null, null, BeyTypes.getByName(properties.getProperty("type")));
                } else if (properties.get("part").equals("driver")) {
                    new ItemBeyDriver(properties.getProperty("name"), new Integer(properties.getProperty("friction")),
                            new Integer(properties.getProperty("radiusReduction")), null, null,
                            BeyTypes.getByName(properties.getProperty("type")));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BeyCraft.logger.info("items were registered");
    }
}
