package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.items.ItemLauncher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyLauncher extends SlotItemHandler {

	public SlotBeyLauncher(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		boolean isValid = false;

		if(stack.getItem() instanceof ItemLauncher){
			if(!stack.hasTag() || !(ItemStack.read((CompoundNBT) stack.getTag().get("bey")).getItem() instanceof ItemBeyLayer)){
				isValid = true;
			}
		}
		return isValid;
	}

}
