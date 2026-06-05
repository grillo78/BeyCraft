package grillo78.beycraft.items;

import com.google.common.collect.Lists;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.items.burst.Layer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Beycraft.MOD_ID);

    public static final DeferredHolder<Item,Item> BURST_LAYER = register("burst_layer", ()-> new Layer(new Item.Properties()));
    public static final DeferredHolder<Item,Item> BURST_DISC = register("burst_disc", ()-> new Item(new Item.Properties()));
    public static final DeferredHolder<Item,Item> BURST_DRIVER = register("burst_driver", ()-> new Item(new Item.Properties()));

    public static <T extends Item, V extends Supplier<T>> DeferredHolder<Item, T> register(String name, V itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
}
