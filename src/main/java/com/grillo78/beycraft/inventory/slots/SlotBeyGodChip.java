package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyGTChip;
import com.grillo78.beycraft.items.ItemBeyGodChip;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyGodChip extends SlotItemHandler{

	public SlotBeyGodChip(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof ItemBeyGodChip;
	}
}
