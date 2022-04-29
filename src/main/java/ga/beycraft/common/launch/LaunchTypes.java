package ga.beycraft.common.launch;

import ga.beycraft.Beycraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;

public class LaunchTypes {
    public static final DeferredRegister<LaunchType> LAUNCH_TYPES = DeferredRegister.create(LaunchType.class, Beycraft.MOD_ID);

    public static final LaunchType BASIC_LAUNCH_TYPE = register("basic_launch_type", new LaunchType(Launch::getLaunch, 1,50, null));
    public static final LaunchType RUSH_LAUNCH_TYPE = register("rush_launch_type", new LaunchType(RushLaunch::new, 25,50, new ResourceLocation(Beycraft.MOD_ID, "basic_launch_type")));
    public static final LaunchType SHU_LAUNCH_TYPE = register("shu_launch_type", new LaunchType(RushLaunch::new, 25,75, new ResourceLocation(Beycraft.MOD_ID, "basic_launch_type")));

    private static <T extends LaunchType> T register(String name, T launchType) {
        LAUNCH_TYPES.register(name, () -> launchType);
        return launchType;
    }
}
