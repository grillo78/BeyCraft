package grillo78.beycraft.common.launch;

import grillo78.beycraft.Beycraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Beycraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LaunchType extends ForgeRegistryEntry<LaunchType> {
    public static IForgeRegistry<LaunchType> LAUNCH_TYPES;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        LAUNCH_TYPES = new RegistryBuilder<LaunchType>().setName(new ResourceLocation(Beycraft.MOD_ID, "launch_types")).setType(LaunchType.class).setIDRange(0, 2048).create();
    }

    private final Supplier<Launch> launchSupplier;

    public LaunchType(Supplier<Launch> launchSupplier) {
        this.launchSupplier = launchSupplier;
    }
    public Launch generateLaunch() {
        return launchSupplier.get();
    }
}
