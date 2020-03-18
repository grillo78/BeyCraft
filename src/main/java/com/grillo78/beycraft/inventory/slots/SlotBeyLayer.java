package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.items.ItemBeyLayerDual;

import net.minecraft.item.ItemStack;
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
		return stack.getItem() instanceof ItemBeyLayer && (((ItemBeyLayer) stack.getItem()).getRotationDirection() == rotation || stack.getItem()instanceof ItemBeyLayerDual);
	}

}
