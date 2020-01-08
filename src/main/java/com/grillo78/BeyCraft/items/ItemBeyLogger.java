package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.Reference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBeyLogger extends Item{

	public ItemBeyLogger(String name) {
		super(new Item.Properties().group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
		setRegistryName(new ResourceLocation(Reference.MODID,name));
		BeyRegistry.ITEMS.add(this);
	}
}
