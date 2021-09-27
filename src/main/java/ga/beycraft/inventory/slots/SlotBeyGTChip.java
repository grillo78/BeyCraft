package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemBeyGTChip;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyGTChip extends SlotItemHandler {

    public SlotBeyGTChip(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ItemBeyGTChip;
    }
}
