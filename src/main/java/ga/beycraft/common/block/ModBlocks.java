package ga.beycraft.common.block;

import ga.beycraft.Beycraft;
import ga.beycraft.common.item.ModItems;
import ga.beycraft.common.tab.BeycraftCreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Beycraft.MOD_ID);

    public static StadiumBlock STADIUM = register("stadium", new StadiumBlock(BlockBehaviour.Properties.of(Material.METAL)));

    private static <T extends Block> T register(String name, T block) {
        BLOCKS.register(name, () -> block);
        ModItems.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(BeycraftCreativeModeTab.INSTANCE)));
        return block;
    }
}
