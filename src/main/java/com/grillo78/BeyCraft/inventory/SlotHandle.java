package com.grillo78.BeyCraft.inventory;

import com.grillo78.BeyCraft.items.ItemLauncherHandle;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotHandle extends SlotItemHandler{

	public SlotHandle(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemLauncherHandle;
	}
	
}
