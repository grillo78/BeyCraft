package ga.beycraft.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static class Common {
        public final ForgeConfigSpec.BooleanValue downloadDefaultPack;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("common"); {
                this.downloadDefaultPack = builder.comment("If true the mod will download the default content pack every time that start the game").define("downloadDefaultPack", true);
            }
            builder.pop();
        }
    }

    public static final ForgeConfigSpec commonSpec;
    public static final Config.Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
