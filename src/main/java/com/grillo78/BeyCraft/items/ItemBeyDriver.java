package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.abilities.IAbility;
import com.grillo78.BeyCraft.util.BeyTypes;

public class ItemBeyDriver extends ItemBeyPart {

	public float height;
	public float friction;
	public float radiusReduction;

	public ItemBeyDriver(String name, float height, float friction, float radiusReduction, IAbility primaryAbility,
			IAbility secundaryAbility, BeyTypes type) {
		super(name, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTDRIVERS);
		// TODO Auto-generated constructor stub
		this.radiusReduction = radiusReduction;
		this.friction = friction;
		BeyRegistry.ITEMSDRIVER.add(this);
	}
}
