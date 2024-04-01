package com.beycraft.common.container.slot;

import com.beycraft.common.item.LayerItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BeltBeySlot extends SlotItemHandler {


    public BeltBeySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof LayerItem && ((LayerItem) stack.getItem()).isBeyAssembled(stack);
    }
}
