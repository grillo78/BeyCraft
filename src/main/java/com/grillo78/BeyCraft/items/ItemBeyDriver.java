package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.abilities.Ability;
import com.grillo78.BeyCraft.util.BeyTypes;

import net.minecraft.item.Item;

public class ItemBeyDriver extends ItemBeyPart {

	public float friction;
	public float radiusReduction;

	public ItemBeyDriver(String name, float friction, float radiusReduction, Ability primaryAbility,
			Ability secundaryAbility, BeyTypes type) {
		super(name, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTDRIVERS,new Item.Properties());
		this.radiusReduction = radiusReduction;
		this.friction = friction;
		BeyRegistry.ITEMSDRIVER.add(this);
	}
}
