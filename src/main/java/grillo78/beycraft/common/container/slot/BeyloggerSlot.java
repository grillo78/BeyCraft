package grillo78.beycraft.common.container.slot;

import grillo78.beycraft.common.item.BeyloggerItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BeyloggerSlot extends SlotItemHandler {
    public BeyloggerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof BeyloggerItem;
    }
}
