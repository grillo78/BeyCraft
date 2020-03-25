package com.grillo78.beycraft.util;

import com.google.common.collect.Lists;
import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.abilities.Absorb;
import com.grillo78.beycraft.abilities.MultiType;
import com.grillo78.beycraft.items.*;
import net.minecraft.item.Item;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ItemCreator {

    public static void removeFolder(File folder){
        String[]entries = folder.list();
        for(String s: entries){
            File currentFile = new File(folder.getPath(),s);
            if(currentFile.isDirectory()){
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
        BeyCraft.logger.info(zipFiles.length + " items was found in the folder");
        FileReader fileReader;

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
                        if (properties.get("part").equals("layer")) {
                            new ItemBeyLayer(entry.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("rotationDirection")),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")), new Float(properties.getProperty("burst")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties), getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties), BeyTypes.getByName(properties.getProperty("type")));
                        } else if (properties.get("part").equals("disc")) {
                            new ItemBeyDisc(entry.getName().replace(".properties", ""), new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")), new Float(properties.getProperty("speed")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties), getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties), new Item.Properties());
                        } else if (properties.get("part").equals("frame")) {
                            new ItemBeyFrame(entry.getName()
                                    .replace(".properties", ""));
                        } else if (properties.get("part").equals("framedisc")) {
                            new ItemBeyDiscFrame(entry.getName().replace(".properties", ""), new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")), new Float(properties.getProperty("speed")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties), getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties));
                        } else if (properties.get("part").equals("driver")) {
                            new ItemBeyDriver(entry.getName().replace(".properties", ""), new Float(properties.getProperty("friction")),
                                    new Float(properties.getProperty("radiusReduction")), getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties), getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    BeyTypes.getByName(properties.getProperty("type")));
                        }
                        if (isPatchuoliInstalled()) {
                            File entryBookFolder = new File( "patchouli_books/beycraft/en_us/entries/" + entry.getName().replace(".properties", ""));
                            entryBookFolder.mkdir();
                            File jsonFile = new File(entryBookFolder.getPath() + "/" + entry.getName().replace(".properties", ".json"));
                            FileWriter fileWriter = new FileWriter(jsonFile);
                            String description = "";
                            if (properties.contains("description")) {
                                description = properties.getProperty("description");
                            }
                            if (!properties.get("part").equals("framedisc")) {
                                fileWriter.write("{\n" +
                                        "  \"name\": \"" + properties.get("name") + "\",\n" +
                                        "  \"icon\": \"beycraft:" + entry.getName().replace(".properties", "") + "\",\n" +
                                        "  \"category\": \"" + properties.get("part") + "s_category\",\n" +
                                        "  \"pages\": [\n" +
                                        "    {\n" +
                                        "      \"type\": \"entity\",\n" +
                                        "      \"entity\": \"minecraft:item{Item:{id:\'beycraft:" + entry.getName().replace(".properties", "") + "',Damage:1,Count:1b}}\",\n" +
                                        "      \"scale\": 5," +
                                        "      \"offset\": -0.25,"+
                                        "      \"text\": \"" + description + "\"\n" +
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
                                        "      \"type\": \"entity\",\n" +
                                        "      \"entity\": \"minecraft:item{Item:{id:\'beycraft:" + entry.getName().replace(".properties", "") + "',Damage:1,Count:1b}}\",\n" +
                                        "      \"scale\": 5," +
                                        "      \"offset\": -0.25,"+
                                        "      \"text\": \"" + description + "\"\n" +
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

        if (isPatchuoliInstalled()) {
            try {
                File bookFolder = new File("patchouli_books/beycraft");
                if (bookFolder.exists()) {
                    removeFolder(bookFolder);
                }
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
                File categoryFile;
                if(!BeyRegistry.ITEMSLAYER.isEmpty()){
                    categoryFile = new File(bookFolder.getPath() + "/en_us/categories/layers_category.json");
                    fileWriter = new FileWriter(categoryFile);
                    fileWriter.write("{\n" +
                            "  \"name\": \"Layers\",\n" +
                            "  \"description\": \"This is the category for all the info about layers\",\n" +
                            "  \"icon\": \""+ BeyRegistry.ITEMSLAYER.get(0) +"\"\n" +
                            "}");
                    fileWriter.flush();
                    fileWriter.close();
                }
                if(!BeyRegistry.ITEMSDISCLIST.isEmpty()){
                    categoryFile = new File(bookFolder.getPath() + "/en_us/categories/disks_category.json");
                    fileWriter = new FileWriter(categoryFile);
                    fileWriter.write("{\n" +
                            "  \"name\": \"Discs\",\n" +
                            "  \"description\": \"This is the category for all the info about disks\",\n" +
                            "  \"icon\": \""+ BeyRegistry.ITEMSDISCLIST.get(0) +"\"\n" +
                            "}");
                    fileWriter.flush();
                    fileWriter.close();
                }
                if(!BeyRegistry.ITEMSDRIVER.isEmpty()){
                    categoryFile = new File(bookFolder.getPath() + "/en_us/categories/drivers_category.json");
                    fileWriter = new FileWriter(categoryFile);
                    fileWriter.write("{\n" +
                            "  \"name\": \"Drivers\",\n" +
                            "  \"description\": \"This is the category for all the info about drivers\",\n" +
                            "  \"icon\": \""+ BeyRegistry.ITEMSDRIVER.get(0) +"\"\n" +
                            "}");
                    fileWriter.flush();
                    fileWriter.close();
                }
                if(!BeyRegistry.ITEMSFRAMELIST.isEmpty()){
                    categoryFile = new File(bookFolder.getPath() + "/en_us/categories/frames_category.json");
                    fileWriter = new FileWriter(categoryFile);
                    fileWriter.write("{\n" +
                            "  \"name\": \"Frames\",\n" +
                            "  \"description\": \"This is the category for all the info about frames\",\n" +
                            "  \"icon\": \"\"\n" +
                            "}");
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
