package com.grillo78.BeyCraft.tab;

import com.grillo78.BeyCraft.BeyRegistry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BeyCraftLayersTab extends ItemGroup{
	
	public BeyCraftLayersTab(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(BeyRegistry.ACHILLESA4);
	}
}
