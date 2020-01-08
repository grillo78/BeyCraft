package com.grillo78.BeyCraft.items;

import net.minecraft.item.ItemStack;

public class ItemBeyDiskFrame extends ItemBeyDisk{

	private ItemBeyDisk disk;
	
	public ItemBeyDiskFrame(String name, float height, ItemBeyDisk disk) {
		super(name, height);
		this.disk = disk;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return new ItemStack(disk);
	}
}
