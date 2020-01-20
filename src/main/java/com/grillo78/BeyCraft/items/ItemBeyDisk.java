package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.abilities.Ability;
import com.grillo78.BeyCraft.util.BeyTypes;

import net.minecraft.item.Item;

public class ItemBeyDisk extends ItemBeyPart{

	public ItemBeyDisk(String name, int attack, int defense, int weight,
			int burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type, Item.Properties properties) {
		super(name, null, null, null, BeyCraft.BEYCRAFTDISKS,properties);
		BeyRegistry.ITEMSDISK.put(name, this);
		BeyRegistry.ITEMSDISKLIST.add(this);
	}
}
