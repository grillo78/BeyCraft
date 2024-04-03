package grillo78.beycraft.common.container.slot;

import grillo78.beycraft.common.item.DriverItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DriverSlot extends SlotItemHandler {
    public DriverSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof DriverItem;
    }
}
