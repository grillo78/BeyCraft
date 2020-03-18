package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.util.BeyTypes;

import net.minecraft.item.Item;

public class ItemBeyDisk extends ItemBeyPart{

	public ItemBeyDisk(String name, int attack, int defense, int weight,
			int burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type, Item.Properties properties) {
		super(name, null, null, null, BeyCraft.BEYCRAFTDISKS,properties);
		BeyRegistry.ITEMSDISK.put(name, this);
		BeyRegistry.ITEMSDISKLIST.add(this);
	}
}
