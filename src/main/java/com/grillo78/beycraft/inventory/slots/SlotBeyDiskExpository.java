package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyDisc;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotBeyDiskExpository extends Slot {

	public SlotBeyDiskExpository(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemBeyDisc;
	}
}
