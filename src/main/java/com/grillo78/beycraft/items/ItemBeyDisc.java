package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;

import net.minecraft.item.Item;

public class ItemBeyDisc extends ItemBeyPart{

	private float weight;
	private float attack;
	private float defense;

	public ItemBeyDisc(String name, float attack, float defense, float weight,
					   float speed, Ability primaryAbility, Ability secondaryAbility, Item.Properties properties) {
		super(name, null, primaryAbility, secondaryAbility, BeyCraft.BEYCRAFTDISKS,properties);
		this.weight = weight;
		BeyRegistry.ITEMSDISCLIST.add(this);
	}

	public float getWeight() {
		return weight;
	}
}
