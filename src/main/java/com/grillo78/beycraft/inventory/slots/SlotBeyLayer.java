package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyDisc;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.items.ItemBeyLayerDual;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyLayer extends SlotItemHandler {

	private int rotation;
	public SlotBeyLayer(IItemHandler itemHandler, int index, int xPosition, int yPosition, int rotation) {
		super(itemHandler, index, xPosition, yPosition);
		this.rotation = rotation;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		boolean[] isValid = {false};
		if (stack.getItem() instanceof ItemBeyLayer && (((ItemBeyLayer) stack.getItem()).getRotationDirection() == rotation || stack.getItem()instanceof ItemBeyLayerDual)){
			stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
				if(h.getStackInSlot(0).getItem() instanceof ItemBeyDisc && h.getStackInSlot(1).getItem() instanceof ItemBeyDriver){
					isValid[0] = true;
				}
			});
		}
		return isValid[0];
	}

}
