package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.items.ItemLauncher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyLauncher extends SlotItemHandler {

	public SlotBeyLauncher(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		final boolean[] isValid = {false};

		if(stack.getItem() instanceof ItemLauncher){
			stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
				BeyCraft.logger.info(h.getStackInSlot(0).getItem());
				if(!(h.getStackInSlot(0).getItem() instanceof ItemBeyLayer)){
					isValid[0] = true;
				}
			});
		}
		return isValid[0];
	}

}
