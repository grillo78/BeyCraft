package grillo78.beycraft.items.components;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.data.parts.Beypart;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Beycraft.MOD_ID);

    public static final Supplier<DataComponentType<ResourceLocation>> BEYPART = DATA_COMPONENTS.registerComponentType(
            "beypart", builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> BURST_DISC = DATA_COMPONENTS.registerComponentType(
            "burst_disc", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> BURST_DRIVER = DATA_COMPONENTS.registerComponentType(
            "burst_driver", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
}
