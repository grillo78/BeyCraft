package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;

import net.minecraft.item.Item;

public class ItemBeyFrame extends ItemBeyPart{

	
	public ItemBeyFrame(String name) {
		super(name, null, null, null, BeyCraft.BEYCRAFTDISKS,new Item.Properties());
		BeyRegistry.ITEMSFRAMELIST.add(this);
	}
}
