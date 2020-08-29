package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyGTChip;
import com.grillo78.beycraft.items.ItemBeyGTWeight;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyGTWeight extends SlotItemHandler{

	public SlotBeyGTWeight(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		boolean hasWeight = false;
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h-> System.out.println(h.getStackInSlot(2).getItem()));
		return stack.getItem() instanceof ItemBeyGTWeight && !hasWeight;
	}
}
