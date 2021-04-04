package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyDisc;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.grillo78.beycraft.items.ItemBeyLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyBothLayer extends SlotItemHandler {

	public SlotBeyBothLayer(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		final boolean[] isValid = {false};

		if(stack.getItem() instanceof ItemBeyLayer){
			stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
				if (h.getSlots() == 2) {
					if (ItemStack.of((CompoundNBT) stack.getTag().get("disc")).getItem() instanceof ItemBeyDisc && ItemStack.of((CompoundNBT) stack.getTag().get("driver")).getItem() instanceof ItemBeyDriver) {
						isValid[0] = true;
					}
				} else {
					if (ItemStack.of((CompoundNBT) stack.getTag().get("disc")).getItem() instanceof ItemBeyDisc && ItemStack.of((CompoundNBT) stack.getTag().get("driver")).getItem() instanceof ItemBeyDriver) {
						isValid[0] = true;
					}
				}
			});
		}
		return isValid[0];
	}

}
