package com.grillo78.BeyCraft.tab;

import com.grillo78.BeyCraft.BeyRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class BeyCraftTab extends CreativeTabs{

	public BeyCraftTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(BeyRegistry.VALTRYEKV2);
	}
}
