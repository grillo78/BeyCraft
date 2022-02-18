package ga.beycraft.common.tab;

import ga.beycraft.Beycraft;
import ga.beycraft.common.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BeycraftCreativeModeTab extends CreativeModeTab {
    public static final CreativeModeTab INSTANCE = new BeycraftCreativeModeTab(Beycraft.MOD_ID, new ItemStack(ModBlocks.STADIUM.asItem()));
    public static final CreativeModeTab LAYERS = new BeycraftCreativeModeTab("layers", new ItemStack(ModBlocks.STADIUM.asItem()));
    public static final CreativeModeTab DISCS = new BeycraftCreativeModeTab("discs", new ItemStack(ModBlocks.STADIUM.asItem()));
    public static final CreativeModeTab DRIVERS = new BeycraftCreativeModeTab("drivers", new ItemStack(ModBlocks.STADIUM.asItem()));
    private ItemStack iconStack;
    public BeycraftCreativeModeTab(String label, ItemStack iconStack) {
        super(label);
        this.iconStack = iconStack;
    }

    @Override
    public ItemStack makeIcon() {
        return iconStack;
    }
}
