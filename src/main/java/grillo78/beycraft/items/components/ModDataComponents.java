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

    public static final Supplier<DataComponentType<ResourceLocation>> LAUNCHER = DATA_COMPONENTS.registerComponentType(
            "launcher", builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> LAUNCHER_BEY = DATA_COMPONENTS.registerComponentType(
            "launcher_bey", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );

    public static final Supplier<DataComponentType<ResourceLocation>> BEYPART = DATA_COMPONENTS.registerComponentType(
            "beypart", builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> BURST_DISC = DATA_COMPONENTS.registerComponentType(
            "burst_disc", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> BURST_FRAME = DATA_COMPONENTS.registerComponentType(
            "burst_frame", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> BURST_DRIVER = DATA_COMPONENTS.registerComponentType(
            "burst_driver", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );

    public static final Supplier<DataComponentType<ItemContent>> METAL_FACEBOLT = DATA_COMPONENTS.registerComponentType(
            "metal_facebolt", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> METAL_FUSION_WHEEL = DATA_COMPONENTS.registerComponentType(
            "metal_fusion_wheel", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> METAL_SPIN_TRACK = DATA_COMPONENTS.registerComponentType(
            "metal_spin_track", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> METAL_PERFORMANCE_TIP = DATA_COMPONENTS.registerComponentType(
            "metal_performance_tip", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> X_RATCHET = DATA_COMPONENTS.registerComponentType(
            "x_ratchet", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<ItemContent>> X_BIT = DATA_COMPONENTS.registerComponentType(
            "x_bit", builder -> builder.persistent(ItemContent.CODEC).networkSynchronized(ItemContent.STREAM_CODEC).cacheEncoding()
    );
}
