package grillo78.beycraft.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class Config {

    public static class PreSetup {
        public boolean downloadDefaultPack = true;

        public PreSetup() {
            File configFile = new File("config/beycraft_pre_setup.properties");
            Properties config = new Properties();
            try {
                if (!configFile.exists()) {
                    configFile.createNewFile();
                }
                config.load(new FileReader(configFile));
                if (config.isEmpty()) {
                    config.setProperty("downloadDefaultPack", "true");
                }
                downloadDefaultPack = Boolean.valueOf(config.getProperty("downloadDefaultPack"));
                config.store(new FileWriter(configFile),"");
            } catch (Exception e) {
            }
        }
    }

    public static class Common {

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("common");
            {
            }
            builder.pop();
        }
    }

    public static final ForgeConfigSpec commonSpec;
    public static final Config.Common COMMON;
    public static final Config.PreSetup PRE_SETUP = new PreSetup();

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
