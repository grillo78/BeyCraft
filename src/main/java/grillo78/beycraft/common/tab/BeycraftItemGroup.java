package grillo78.beycraft.common.tab;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.common.block.ModBlocks;
import grillo78.beycraft.common.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BeycraftItemGroup extends ItemGroup {
    public static final ItemGroup INSTANCE = new BeycraftItemGroup(Beycraft.MOD_ID);
    public static final ItemGroup LAYERS = new BeycraftItemGroup("layers");
    public static final ItemGroup DISCS = new BeycraftItemGroup("discs");
    public static final ItemGroup DRIVERS = new BeycraftItemGroup("drivers");
    private String label;
    public BeycraftItemGroup(String label) {
        super(label);
        this.label = label;
    }

    @Override
    public ItemStack makeIcon() {
        ItemStack stack;
        switch (label) {
            case "layers":
                stack = new ItemStack(ModItems.LAYERICON.asItem());
                break;
            case "discs":
                stack = new ItemStack(ModItems.DISCICON.asItem());
                break;
            case "drivers":
                stack = new ItemStack(ModItems.DRIVERICON.asItem());
                break;
            case Beycraft.MOD_ID:
                stack = new ItemStack(ModBlocks.STADIUM.asItem());
                break;
            default:
                stack = new ItemStack(ModBlocks.STADIUM.asItem());
        }
        return stack;
    }
}
