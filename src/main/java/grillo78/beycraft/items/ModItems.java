package grillo78.beycraft.items;

import com.google.common.collect.Lists;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.items.burst.*;
import grillo78.beycraft.items.metal.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Beycraft.MOD_ID);

    public static final DeferredHolder<Item,Item> TEST_LAUNCHER = register("test_launcher", ()-> new Launcher(new Item.Properties()));

    public static final DeferredHolder<Item,Item> BURST_LAYER = register("burst_layer", ()-> new Layer(new Item.Properties()));
    public static final DeferredHolder<Item,Item> BURST_DISC = register("burst_disc", ()-> new Disc(new Item.Properties()));
    public static final DeferredHolder<Item,Item> BURST_CORE_DISC = register("burst_core_disc", ()-> new CoreDisc(new Item.Properties()));
    public static final DeferredHolder<Item,Item> BURST_FRAME = register("burst_frame", ()-> new Frame(new Item.Properties()));
    public static final DeferredHolder<Item,Item> BURST_DRIVER = register("burst_driver", ()-> new Driver(new Item.Properties()));

    public static final DeferredHolder<Item,Item> METAL_FACEBOLT = register("metal_facebolt", ()-> new Facebolt(new Item.Properties()));
    public static final DeferredHolder<Item,Item> METAL_ENERGY_RING = register("metal_energy_ring", ()-> new EnergyRing(new Item.Properties()));
    public static final DeferredHolder<Item,Item> METAL_FUSION_WHEEL = register("metal_fusion_wheel", ()-> new FusionWheel(new Item.Properties()));
    public static final DeferredHolder<Item,Item> METAL_SPIN_TRACK = register("metal_spin_track", ()-> new SpinTrack(new Item.Properties()));
    public static final DeferredHolder<Item,Item> METAL_PERFORMANCE_TIP = register("metal_performance_tip", ()-> new PerformanceTip(new Item.Properties()));

    public static <T extends Item, V extends Supplier<T>> DeferredHolder<Item, T> register(String name, V itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
}
