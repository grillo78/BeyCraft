package ga.beycraft.util;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigManager {

	private static final File CONFIG_FILE = new File("config/beycraft.properties");
	private static final Properties CONFIG = new Properties();

    public static File getConfigFile() {
        return CONFIG_FILE;
    }

    public static Properties getConfig() {
        return CONFIG;
    }

    public static void load() {
	    try {
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();
            }
            CONFIG.load(new FileReader(CONFIG_FILE));
            if(CONFIG.isEmpty()){
                CONFIG.setProperty("onlyStadium", "true");
                CONFIG.setProperty("blockBlackList", "");
                CONFIG.setProperty("downloadDefaultPack", "true");
            }
            if(!CONFIG.containsKey("downloadDefaultPack"))
                CONFIG.setProperty("downloadDefaultPack", "true");
            CONFIG.store(new FileWriter(CONFIG_FILE),"");
        }catch (IOException e){

        }
	}

	public static boolean isOnlyStadium(){
	    return Boolean.valueOf(CONFIG.getProperty("onlyStadium"));
    }

    public static boolean downloadDefaultPack(){
        return Boolean.valueOf(CONFIG.getProperty("downloadDefaultPack"));
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

    public static String getToken(){
	    return CONFIG.getProperty("token");
    }
}
