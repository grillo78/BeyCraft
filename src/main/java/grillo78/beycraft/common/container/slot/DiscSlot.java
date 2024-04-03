package grillo78.beycraft.common.container.slot;

import grillo78.beycraft.common.item.DiscItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DiscSlot extends SlotItemHandler {
    public DiscSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof DiscItem;
    }
}
