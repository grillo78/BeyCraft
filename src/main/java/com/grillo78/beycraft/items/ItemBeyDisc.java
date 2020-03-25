package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;

import net.minecraft.item.Item;

public class ItemBeyDisc extends ItemBeyPart{

	public ItemBeyDisc(String name, float attack, float defense, float weight,
					   float speed, Ability primaryAbility, Ability secundaryAbility, Item.Properties properties) {
		super(name, null, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTDISKS,properties);
		BeyRegistry.ITEMSDISC.put(name, this);
		BeyRegistry.ITEMSDISCLIST.add(this);
	}
}
