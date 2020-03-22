package com.grillo78.beycraft.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.collect.Lists;
import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.abilities.Absorb;
import com.grillo78.beycraft.abilities.MultiType;
import com.grillo78.beycraft.items.ItemBeyDisk;
import com.grillo78.beycraft.items.ItemBeyDiskFrame;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.grillo78.beycraft.items.ItemBeyFrame;
import com.grillo78.beycraft.items.ItemBeyLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.resources.FilePack;
import net.minecraft.resources.FolderPack;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.loading.FMLLoader;

public class ItemCreator {

    public static void getItemsFromFolder() {
        File itemsFolder = new File(Minecraft.getInstance().gameDir, "BeyParts");
        if (isPatchuoliInstalled()) {
            File bookFolder = new File(Minecraft.getInstance().gameDir, "patchouli_books/beycraft");
            if (bookFolder.exists()) bookFolder.delete();
        }
        if (!itemsFolder.exists()) {
            itemsFolder.mkdir();
        }
        File[] zipFiles = itemsFolder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".zip");
            }
        });
        BeyCraft.logger.info(zipFiles.length + " items was found in the folder");
        FileReader fileReader;



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

        try {
            for (File file : zipFiles) {
                ZipFile zipFile = new ZipFile(file);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()){
                    ZipEntry entry = entries.nextElement();
                    System.out.printf("File: %s Size %d ", entry.getName(), entry.getSize());
                    if(entry.getName().endsWith(".properties")){
                        InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(entry));
                        Properties properties = new Properties();
                        properties.load(reader);
                        if (properties.get("part").equals("layer")) {
                            new ItemBeyLayer(entry.getName().replace(".properties", ""),
                                    new Integer(properties.getProperty("rotationDirection")),
                                    new Integer(properties.getProperty("attack")),
                                    new Integer(properties.getProperty("defense")),
                                    new Integer(properties.getProperty("weight")), new Integer(properties.getProperty("burst")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties), getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties), BeyTypes.getByName(properties.getProperty("type")));
                        } else if (properties.get("part").equals("disk")) {
                            new ItemBeyDisk(entry.getName().replace(".properties", ""), new Integer(properties.getProperty("attack")),
                                    new Integer(properties.getProperty("defense")),
                                    new Integer(properties.getProperty("weight")), new Integer(properties.getProperty("burst")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties), getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties), BeyTypes.getByName(properties.getProperty("type")), new Item.Properties());
                        } else if (properties.get("part").equals("frame")) {
                            new ItemBeyFrame(entry.getName().replace(".properties", ""));
                        } else if (properties.get("part").equals("framedisk")) {
                            new ItemBeyDiskFrame(entry.getName().replace(".properties", ""), new Integer(properties.getProperty("attack")),
                                    new Integer(properties.getProperty("defense")),
                                    new Integer(properties.getProperty("weight")), new Integer(properties.getProperty("burst")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties), getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties), BeyTypes.getByName(properties.getProperty("type")));
                        } else if (properties.get("part").equals("driver")) {
                            new ItemBeyDriver(entry.getName().replace(".properties", ""), new Integer(properties.getProperty("friction")),
                                    new Integer(properties.getProperty("radiusReduction")), getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties), getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    BeyTypes.getByName(properties.getProperty("type")));
                        }
                        if (isPatchuoliInstalled()) {
                            File entryBookFolder = new File(Minecraft.getInstance().gameDir, "patchouli_books/beycraft/en_us/entries/" + entry.getName().replace(".properties", ""));
                            entryBookFolder.mkdir();
                            File jsonFile = new File(entryBookFolder.getPath() + "/" + entry.getName().replace(".properties", ".json"));
                            FileWriter fileWriter = new FileWriter(jsonFile);
                            if (!properties.get("part").equals("framedisk")) {
                                fileWriter.write("{\n" +
                                        "  \"name\": \"" + properties.get("name") + "\",\n" +
                                        "  \"icon\": \"beycraft:" + entry.getName().replace(".properties", "") + "\",\n" +
                                        "  \"category\": \"" + properties.get("part") + "s_category\",\n" +
                                        "  \"pages\": [\n" +
                                        "    {\n" +
                                        "      \"type\": \"text\",\n" +
                                        "      \"text\": \"This is a test entry, but it should show up!\"\n" +
                                        "    }\n" +
                                        "  ]\n" +
                                        "}");
                            } else {
                                fileWriter.write("{\n" +
                                        "  \"name\": \"" + properties.get("name") + "\",\n" +
                                        "  \"icon\": \"beycraft:" + entry.getName().replace(".properties", "") + "\",\n" +
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
                    }
                }
                 zipFile.close();
            }
        } catch (IOException e) {

        }
        BeyCraft.logger.info("items were registered");
    }

    private static Ability getFirstAbilityByName(String name, Properties properties) {
        switch (name) {
            case "Absorb":
                return new Absorb();
            case "MultiType":
                return new MultiType(getTypes(properties.getProperty("types").split(",")));
            default:
                return null;
        }
    }

    private static Ability getSecondAbilityByName(String name, Properties properties) {
        switch (name) {
            case "Absorb":
                return new Absorb();
            case "MultiType":
                return new MultiType(getTypes(properties.getProperty("types").split(",")));
            default:
                return null;
        }
    }

    private static List<BeyTypes> getTypes(String[] list) {
        List<BeyTypes> typeList = Lists.newArrayList();

        for (String name : list) {
            typeList.add(BeyTypes.getByName(name));
        }

        return typeList;
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
