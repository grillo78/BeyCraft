package com.grillo78.BeyCraft.util;

import java.io.*;
import java.util.Properties;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
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
        if (isPatchuoliInstalled()) {
            File bookFolder = new File(Minecraft.getInstance().gameDir, "patchouli_books/beycraft");
            if (bookFolder.exists()) bookFolder.delete();
        }
        if (!beycraftResourcePack.exists()) {
            beycraftResourcePack.mkdir();

        }
        if (!itemsFolder.exists()) {
            itemsFolder.mkdir();
        }File packMCMeta = new File(beycraftResourcePack, "pack.mcmeta");
        try {
            packMCMeta.createNewFile();
            FileWriter fileWriter = new FileWriter(packMCMeta);
            fileWriter.write("{\n" +
                    "    \"pack\": {\n" +
                    "        \"description\": \"beycraft resources\",\n" +
                    "        \"pack_format\": 5,\n" +
                    "        \"_comment\": \"A pack_format of 4 requires json lang files. Note: we require v4 pack meta for all mods.\"\n" +
                    "    }\n" +
                    "}\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
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

        if (isPatchuoliInstalled()) {
            try {
                File bookFolder = new File(Minecraft.getInstance().gameDir, "patchouli_books/beycraft");
                if (bookFolder.exists()) bookFolder.delete();
                bookFolder.mkdir();
                File jsonFile = new File(bookFolder.getPath() + "/book.json");
                jsonFile.createNewFile();
                FileWriter fileWriter = new FileWriter(jsonFile);
                fileWriter.write("{\n" +
                        "  \"name\": \"Beycraft\",\n" +
                        "  \"landing_text\": \"Welcome to Beycraft\",\n" +
                        "  \"version\": 1\n" +
                        "}");
                fileWriter.flush();
                fileWriter.close();
                new File(bookFolder.getPath() + "/en_us").mkdir();
                new File(bookFolder.getPath() + "/en_us/categories").mkdir();
                new File(bookFolder.getPath() + "/en_us/entries").mkdir();
                File categoryFile = new File(bookFolder.getPath() + "/en_us/categories/layers_category.json");
                fileWriter = new FileWriter(categoryFile);
                fileWriter.write("{\n" +
                        "  \"name\": \"Layers\",\n" +
                        "  \"description\": \"This is the category for all the info about layers\",\n" +
                        "  \"icon\": \"beycraft:valtryekv2\"\n" +
                        "}");
                fileWriter.flush();
                fileWriter.close();
                categoryFile = new File(bookFolder.getPath() + "/en_us/categories/disks_category.json");
                fileWriter = new FileWriter(categoryFile);
                fileWriter.write("{\n" +
                        "  \"name\": \"Disks\",\n" +
                        "  \"description\": \"This is the category for all the info about disks\",\n" +
                        "  \"icon\": \"beycraft:boostdisk\"\n" +
                        "}");
                fileWriter.flush();
                fileWriter.close();
                categoryFile = new File(bookFolder.getPath() + "/en_us/categories/drivers_category.json");
                fileWriter = new FileWriter(categoryFile);
                fileWriter.write("{\n" +
                        "  \"name\": \"Drivers\",\n" +
                        "  \"description\": \"This is the category for all the info about drivers\",\n" +
                        "  \"icon\": \"beycraft:variabledriver\"\n" +
                        "}");
                fileWriter.flush();
                fileWriter.close();
                categoryFile = new File(bookFolder.getPath() + "/en_us/categories/frames_category.json");
                fileWriter = new FileWriter(categoryFile);
                fileWriter.write("{\n" +
                        "  \"name\": \"Frames\",\n" +
                        "  \"description\": \"This is the category for all the info about frames\",\n" +
                        "  \"icon\": \"\"\n" +
                        "}");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                if (isPatchuoliInstalled()) {
                    File entryBookFolder = new File(Minecraft.getInstance().gameDir, "patchouli_books/beycraft/en_us/entries/" + properties.getProperty("name"));
                    entryBookFolder.mkdir();
                    File jsonFile = new File(entryBookFolder.getPath() + "/" + properties.get("name") + ".json");
                    FileWriter fileWriter = new FileWriter(jsonFile);
                    if(!properties.get("part").equals("framedisk")){
                        fileWriter.write("{\n" +
                                "  \"name\": \"" + properties.get("name") + "\",\n" +
                                "  \"icon\": \"beycraft:" + properties.get("name") + "\",\n" +
                                "  \"category\": \"" + properties.get("part") + "s_category\",\n" +
                                "  \"pages\": [\n" +
                                "    {\n" +
                                "      \"type\": \"text\",\n" +
                                "      \"text\": \"This is a test entry, but it should show up!\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}");
                    }else{
                        fileWriter.write("{\n" +
                                "  \"name\": \"" + properties.get("name") + "\",\n" +
                                "  \"icon\": \"beycraft:" + properties.get("name") + "\",\n" +
                                "  \"category\": \"disks_category\",\n" +
                                "  \"pages\": [\n" +
                                "    {\n" +
                                "      \"type\": \"text\",\n" +
                                "      \"text\": \"This is a test entry, but it should show up!\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BeyCraft.logger.info("items were registered");
    }

    private static boolean isPatchuoliInstalled() {
        try {
            Class.forName("vazkii.patchouli.api.PatchouliAPI");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
