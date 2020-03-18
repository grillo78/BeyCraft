package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemHandleAccesory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotHandleAccesory extends SlotItemHandler{

	public SlotHandleAccesory(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemHandleAccesory;
	}
}
