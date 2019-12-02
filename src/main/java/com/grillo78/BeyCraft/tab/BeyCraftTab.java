package com.grillo78.BeyCraft.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BeyCraftTab extends CreativeTabs{

	private Item iconItem;
	public BeyCraftTab(String label, Item item) {
		super(label);
		iconItem = item;
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(iconItem);
	}
}
