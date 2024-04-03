package grillo78.beycraft.common.stats;

import grillo78.beycraft.Beycraft;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class CustomStats {
    public static final ResourceLocation LAUNCH = registerCustomStat("launch");

    private static ResourceLocation registerCustomStat(String name) {
        ResourceLocation resourcelocation = new ResourceLocation(Beycraft.MOD_ID, name);
        Registry.register(Registry.CUSTOM_STAT, name, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, IStatFormatter.DEFAULT);
        return resourcelocation;
    }

    public static void init(){}
}
