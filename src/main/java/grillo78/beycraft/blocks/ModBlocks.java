package grillo78.beycraft.blocks;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Beycraft.MOD_ID);

    public static final DeferredHolder<Block, Block> STADIUM = register("stadium", () -> new StadiumBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)), new Item.Properties());

    public static <T extends Block, V extends Supplier<T>> DeferredHolder<Block, T> register(String name, V blockSupplier, Item.Properties properties) {
        DeferredHolder<Block, T> registryObject = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(registryObject.get(), properties));
        return registryObject;
    }

    public static <T extends Block, V extends Supplier<T>> DeferredHolder<Block, T> register(String name, V blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }
}
