package ga.beycraft.common.launch;

import ga.beycraft.Beycraft;
import net.minecraftforge.registries.DeferredRegister;

public class LaunchTypes {
    public static final DeferredRegister<LaunchType> LAUNCH_TYPES = DeferredRegister.create(LaunchType.class, Beycraft.MOD_ID);

    public static final LaunchType BASIC_LAUNCH_TYPE = register("basic_launch_type", new LaunchType(Launch::new, 5,10));

    private static <T extends LaunchType> T register(String name, T launchType) {
        LAUNCH_TYPES.register(name, () -> launchType);
        return launchType;
    }
}
