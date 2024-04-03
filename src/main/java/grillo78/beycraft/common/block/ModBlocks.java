package grillo78.beycraft.common.block;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.common.item.ModItems;
import grillo78.beycraft.common.tab.BeycraftItemGroup;
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
    public static final Block BATTLE_INFORMER = register("battle_informer", new BattleInformerBlock(Material.METAL));

    public static StadiumBlock STADIUM = register("stadium", new StadiumBlock(AbstractBlock.Properties.of(Material.METAL)));
    public static BeycreatorBlock BEYCREATOR = register("beycreator", new BeycreatorBlock(AbstractBlock.Properties.of(Material.METAL)));
    public static ExpositoryBlock EXPOSITORY = register("expository", new ExpositoryBlock(AbstractBlock.Properties.of(Material.METAL)));

    private static <T extends Block> T register(String name, T block) {
        BLOCKS.register(name, () -> block);
        ModItems.ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(BeycraftItemGroup.INSTANCE)));
        return block;
    }
}
