package com.grillo78.beycraft.util;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.collect.Lists;
import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.abilities.Absorb;
import com.grillo78.beycraft.abilities.MultiMode;
import com.grillo78.beycraft.abilities.MultiType;
import com.grillo78.beycraft.items.*;

import net.minecraft.item.Item;

public class ItemCreator {

    public static void removeFolder(File folder) {
        String[] entries = folder.list();
        for (String s : entries) {
            File currentFile = new File(folder.getPath(), s);
            if (currentFile.isDirectory()) {
                removeFolder(currentFile);
            }
            currentFile.delete();
        }
        folder.delete();
    }

    public static void getItemsFromFolder() {
        File itemsFolder = new File("BeyParts");
        if (!itemsFolder.exists()) {
            itemsFolder.mkdir();
        }
        File[] zipFiles = itemsFolder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".zip");
            }
        });
        BeyCraft.logger.info(zipFiles.length + " packs was found in BeyParts folder");

        if (isPatchuoliInstalled()) {
            try {
                File patchouli_booksFolder = new File("patchouli_books");
                if (!patchouli_booksFolder.exists()) {
                    patchouli_booksFolder.mkdir();
                }
                File bookFolder = new File("patchouli_books/beycraft");
                if (bookFolder.exists()) {
                    removeFolder(bookFolder);
                }

                bookFolder.mkdir();
                File jsonFile = new File(bookFolder.getPath() + "/book.json");
                jsonFile.createNewFile();
                FileWriter fileWriter = new FileWriter(jsonFile);
                fileWriter.write("{\n" + "  \"name\": \"Beycraft\",\n"
                        + "  \"landing_text\": \"Welcome to Beycraft\",\n" + "  \"version\": 1\n" + "}");
                fileWriter.flush();
                fileWriter.close();
                new File(bookFolder.getPath() + "/en_us").mkdir();
                new File(bookFolder.getPath() + "/en_us/categories").mkdir();
                new File(bookFolder.getPath() + "/en_us/entries").mkdir();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            for (File file : zipFiles) {
                ZipFile zipFile = new ZipFile(file);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    System.out.printf("File: %s Size %d ", entry.getName(), entry.getSize());
                    if (entry.getName().endsWith(".properties")) {
                        InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(entry));
                        Properties properties = new Properties();
                        properties.load(reader);
                        switch ((String) properties.get("part")) {
                            case "layer":
                                new ItemBeyLayer(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("rotationDirection")),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        new Float(properties.getProperty("burst")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "layerDual":
                                new ItemBeyLayerDual(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        new Float(properties.getProperty("burst")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "disc":
                                new ItemBeyDisc(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null, new Item.Properties());
                                break;
                            case "frame":
                                new ItemBeyFrame(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "framedisc":
                                new ItemBeyDiscFrame(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        new Float(properties.getProperty("frameRotation")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "driver":
                                new ItemBeyDriver(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("friction")),
                                        new Float(properties.getProperty("radiusReduction")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "Godlayer":
                                new ItemBeyLayerGod(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("rotationDirection")),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "GTlayer":
                                new ItemBeyLayerGT(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("rotationDirection")),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "GTlayerDual":
                                new ItemBeyLayerGTDual(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;

                            case "GTlayerNoWeight":
                                new ItemBeyLayerGTNoWeight(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("rotationDirection")),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "GTlayerDualNoWeight":
                                new ItemBeyLayerGTDualNoWeight(entry.getName().replace(".properties", ""),
                                        new Float(properties.getProperty("attack")),
                                        new Float(properties.getProperty("defense")),
                                        new Float(properties.getProperty("weight")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "GTchip":
                                new ItemBeyGTChip(entry.getName().replace(".properties", ""),
                                        properties.containsKey("weight") ? new Float(properties.getProperty("weight")) : 0,
                                        properties.containsKey("burst") ? new Float(properties.getProperty("burst")) : 2);
                                break;
                            case "GTWeight":
                                new ItemBeyGTWeight(entry.getName().replace(".properties", ""),
                                        Float.valueOf(properties.getProperty("weight")));
                                break;
                            case "GodChip":
                                new ItemBeyGodChip(entry.getName().replace(".properties", ""),
                                        Float.valueOf(properties.getProperty("weight")));
                                break;
                        }
                        if (isPatchuoliInstalled()) {
                            File entryBookFolder = new File("patchouli_books/beycraft/en_us/entries/"
                                    + entry.getName().replace(".properties", ""));
                            entryBookFolder.mkdir();
                            File jsonFile = new File(
                                    entryBookFolder.getPath() + "/" + entry.getName().replace(".properties", ".json"));
                            FileWriter fileWriter = new FileWriter(jsonFile);
                            String description = "";
                            if (properties.contains("description")) {
                                description = properties.getProperty("description");
                            }
                            switch ((String) properties.get("part")) {
                                case "framedisc":
                                    fileWriter.write("{\n" + "  \"name\": \"" + properties.get("name") + "\",\n"
                                            + "  \"icon\": \"beycraft:" + entry.getName().replace(".properties", "")
                                            + "\",\n" + "  \"category\": \"discs_category\",\n" + "  \"pages\": [\n"
                                            + "    {\n" + "      \"type\": \"entity\",\n"
                                            + "      \"entity\": \"minecraft:item{Item:{id:\'beycraft:"
                                            + entry.getName().replace(".properties", "") + "',Damage:1,Count:1b}}\",\n"
                                            + "      \"scale\": 5," + "      \"offset\": -0.25," + "      \"text\": \""
                                            + description + "\"\n" + "    }\n" + "  ]\n" + "}");
                                    break;
                                case "GTlayer":
                                case "GTlayerDual":
                                case "GTlayerNoWeight":
                                case "GTlayerDualNoWeight":
                                case "GTchip":
                                case "GTWeight":
                                case "layerDual":
                                    fileWriter.write("{\n" + "  \"name\": \"" + properties.get("name") + "\",\n"
                                            + "  \"icon\": \"beycraft:" + entry.getName().replace(".properties", "")
                                            + "\",\n" + "  \"category\": \"layers_category\",\n" + "  \"pages\": [\n"
                                            + "    {\n" + "      \"type\": \"entity\",\n"
                                            + "      \"entity\": \"minecraft:item{Item:{id:\'beycraft:"
                                            + entry.getName().replace(".properties", "") + "',Damage:1,Count:1b}}\",\n"
                                            + "      \"scale\": 5," + "      \"offset\": -0.25," + "      \"text\": \""
                                            + description + "\"\n" + "    }\n" + "  ]\n" + "}");
                                    break;
                                default:
                                    fileWriter.write("{\n" + "  \"name\": \"" + properties.get("name") + "\",\n"
                                            + "  \"icon\": \"beycraft:" + entry.getName().replace(".properties", "")
                                            + "\",\n" + "  \"category\": \"" + properties.get("part") + "s_category\",\n"
                                            + "  \"pages\": [\n" + "    {\n" + "      \"type\": \"entity\",\n"
                                            + "      \"entity\": \"minecraft:item{Item:{id:\'beycraft:"
                                            + entry.getName().replace(".properties", "") + "',Damage:1,Count:1b}}\",\n"
                                            + "      \"scale\": 5," + "      \"offset\": -0.25," + "      \"text\": \""
                                            + description + "\"\n" + "    }\n" + "  ]\n" + "}");
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

        if (isPatchuoliInstalled()) {
            try {
                File bookFolder = new File("patchouli_books/beycraft");

                FileWriter fileWriter;
                File categoryFile;
                if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
                    categoryFile = new File(bookFolder.getPath() + "/en_us/categories/layers_category.json");
                    fileWriter = new FileWriter(categoryFile);
                    fileWriter.write("{\n" + "  \"name\": \"Layers\",\n"
                            + "  \"description\": \"This is the category for all the info about layers\",\n"
                            + "  \"icon\": \"" + BeyRegistry.ITEMSLAYER.get(0) + "\"\n" + "}");
                    fileWriter.flush();
                    fileWriter.close();
                }
                if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
                    categoryFile = new File(bookFolder.getPath() + "/en_us/categories/discs_category.json");
                    fileWriter = new FileWriter(categoryFile);
                    fileWriter.write("{\n" + "  \"name\": \"Discs\",\n"
                            + "  \"description\": \"This is the category for all the info about disks\",\n"
                            + "  \"icon\": \"" + BeyRegistry.ITEMSDISCLIST.get(0) + "\"\n" + "}");
                    fileWriter.flush();
                    fileWriter.close();
                }
                if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
                    categoryFile = new File(bookFolder.getPath() + "/en_us/categories/drivers_category.json");
                    fileWriter = new FileWriter(categoryFile);
                    fileWriter.write("{\n" + "  \"name\": \"Drivers\",\n"
                            + "  \"description\": \"This is the category for all the info about drivers\",\n"
                            + "  \"icon\": \"" + BeyRegistry.ITEMSDRIVER.get(0) + "\"\n" + "}");
                    fileWriter.flush();
                    fileWriter.close();
                }
                if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
                    categoryFile = new File(bookFolder.getPath() + "/en_us/categories/frames_category.json");
                    fileWriter = new FileWriter(categoryFile);
                    fileWriter.write("{\n" + "  \"name\": \"Frames\",\n"
                            + "  \"description\": \"This is the category for all the info about frames\",\n"
                            + "  \"icon\": \"\"\n" + "}");
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BeyCraft.logger.info("Items were registered");
    }

    private static Ability getFirstAbilityByName(String name, Properties properties) {
        switch (name) {
            case "Absorb":
                return new Absorb();
            case "MultiType":
                return new MultiType(getTypes(properties.getProperty("types").split(" ")));
            case "MultiMode":
                return new MultiMode(getModes(properties.getProperty("modes").split(" ")));
            default:
                return null;
        }
    }

    private static Ability getSecondAbilityByName(String name, Properties properties) {
        switch (name) {
            case "Absorb":
                return new Absorb();
            case "MultiType":
                return new MultiType(getTypes(properties.getProperty("types").split(" ")));
            case "MultiMode":
                return new MultiMode(getModes(properties.getProperty("modes").split(" ")));
            default:
                return null;
        }
    }

    private static List<MultiMode.Mode> getModes(String[] list) {
        List<MultiMode.Mode> modeList = Lists.newArrayList();

        for (String mode : list) {
            modeList.add(new MultiMode.Mode(mode));
        }

        return modeList;
    }

    private static List<Type> getTypes(String[] list) {
        List<Type> typeList = Lists.newArrayList();

        for (String type : list) {
            typeList.add(new Type(type));
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
