package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;

public class ItemBeyFrame extends ItemBeyPart{

	
	public ItemBeyFrame(String name, float height) {
		super(name, null, null, null, BeyCraft.BEYCRAFTDISKS);
		BeyRegistry.ITEMS.add(this);
	}
}
