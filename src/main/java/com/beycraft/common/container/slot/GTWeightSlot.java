package com.beycraft.common.container.slot;

import com.beycraft.common.item.DiscItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GTWeightSlot extends SlotItemHandler {
    public GTWeightSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof DiscItem;
    }
}
