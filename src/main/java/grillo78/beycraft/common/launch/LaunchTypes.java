package grillo78.beycraft.common.launch;

import grillo78.beycraft.Beycraft;
import net.minecraftforge.registries.DeferredRegister;

public class LaunchTypes {
    public static final DeferredRegister<LaunchType> LAUNCH_TYPES = DeferredRegister.create(LaunchType.class, Beycraft.MOD_ID);

    public static final LaunchType BASIC_LAUNCH_TYPE = register("basic_launch_type", new LaunchType(Launch::new));
    public static final LaunchType HAND_LAUNCH_TYPE = register("hand_launch_type", new LaunchType(HandSpinningLaunch::new));
    public static final LaunchType BASIC_ATTACK_LAUNCH_TYPE = register("basic_attack_launch_type", new LaunchType(BasicAttackLaunch::new));
    public static final LaunchType FLOWER_ATTACK_LAUNCH_TYPE = register("flower_attack_launch_type", new LaunchType(FlowerAttackLaunch::new));

    private static <T extends LaunchType> T register(String name, T launchType) {
        LAUNCH_TYPES.register(name, () -> launchType);
        return launchType;
    }
}
