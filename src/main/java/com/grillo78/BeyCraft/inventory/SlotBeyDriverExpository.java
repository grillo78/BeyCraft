package com.grillo78.BeyCraft.inventory;

import com.grillo78.BeyCraft.items.ItemBeyDriver;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBeyDriverExpository extends Slot {

	public SlotBeyDriverExpository(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemBeyDriver;
	}
}