package ga.beycraft.common.block;

import ga.beycraft.Beycraft;
import ga.beycraft.common.item.ModItems;
import ga.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Beycraft.MOD_ID);

    public static StadiumBlock STADIUM = register("stadium", new StadiumBlock(AbstractBlock.Properties.of(Material.METAL)));

    private static <T extends Block> T register(String name, T block) {
        BLOCKS.register(name, () -> block);
        ModItems.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(BeycraftItemGroup.INSTANCE)));
        return block;
    }
}
