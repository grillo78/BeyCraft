package com.grillo78.beycraft.items;

import net.minecraft.item.ItemStack;

public class ItemDualLauncher extends ItemLauncher{

	public ItemDualLauncher(String name) {
		super(name,-1);
	}

	@Override
	public int getRotation(ItemStack stack) {
		if(stack.hasTag() && stack.getTag().contains("rotation")){
			return stack.getTag().getInt("rotation");
		}
		return 1;
	}
}
