package com.grillo78.beycraft.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigManager {

	private static final File CONFIG_FILE = new File("config/beycraft.properties");
	private static final Properties CONFIG = new Properties();

	public static void load() {
	    try {
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();
            }
            CONFIG.load(new FileReader(CONFIG_FILE));
            if(CONFIG.isEmpty()){
                CONFIG.setProperty("onlyStadium", "true");
                CONFIG.setProperty("blockBlackList", "");
                CONFIG.store(new FileWriter(CONFIG_FILE),"");
            }
        }catch (IOException e){

        }
	}

	public static boolean isOnlyStadium(){
	    return Boolean.valueOf(CONFIG.getProperty("onlyStadium"));
    }

    public static ArrayList<Block> getBlockBlackList(){
	    ArrayList<Block> blocks = new ArrayList<>();
	    String[] blocksStr = CONFIG.getProperty("blockBlackList").split(",");
	    if(!CONFIG.getProperty("blockBlackList").equals("")){
	        for(String s : blocksStr){
                blocks.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s.split(":")[0],s.split(":")[1])));
            }
        }
	    return blocks;
    }
}
