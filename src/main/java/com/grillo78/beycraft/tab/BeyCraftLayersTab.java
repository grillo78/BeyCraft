package com.grillo78.beycraft.tab;

import com.grillo78.beycraft.BeyRegistry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BeyCraftLayersTab extends ItemGroup{
	
	public BeyCraftLayersTab(String label) {
		super(label);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(BeyRegistry.LAYERICON);
	}
}
