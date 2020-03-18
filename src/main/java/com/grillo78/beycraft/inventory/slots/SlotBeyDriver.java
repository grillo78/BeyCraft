package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyDriver;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyDriver extends SlotItemHandler{

	public SlotBeyDriver(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemBeyDriver;
	}
}
