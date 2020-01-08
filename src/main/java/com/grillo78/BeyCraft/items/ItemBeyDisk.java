package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;

public class ItemBeyDisk extends ItemBeyPart{

	
	public ItemBeyDisk(String name, float height) {
		super(name, null, null, null, BeyCraft.BEYCRAFTDISKS);
		BeyRegistry.ITEMSDISK.add(this);
	}
}
