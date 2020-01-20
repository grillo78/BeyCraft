package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;

import net.minecraft.item.Item;

public class ItemBeyFrame extends ItemBeyPart{

	
	public ItemBeyFrame(String name) {
		super(name, null, null, null, BeyCraft.BEYCRAFTDISKS,new Item.Properties());
		BeyRegistry.ITEMSFRAMELIST.add(this);
	}
}
