package com.grillo78.beycraft.tab;

import com.grillo78.beycraft.BeyRegistry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BeyCraftTab extends ItemGroup{

	
	public BeyCraftTab(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(BeyRegistry.REDLAUNCHER);
	}
}
