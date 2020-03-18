package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.util.BeyTypes;

import net.minecraft.item.Item;

public class ItemBeyDriver extends ItemBeyPart {

	private float friction;
	private float radiusReduction;

	public ItemBeyDriver(String name, float friction, float radiusReduction, Ability primaryAbility,
			Ability secundaryAbility, BeyTypes type) {
		super(name, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTDRIVERS,new Item.Properties());
		this.radiusReduction = radiusReduction;
		this.friction = friction;
		BeyRegistry.ITEMSDRIVER.add(this);
	}

	public float getRadiusReduction() {
		return radiusReduction;
	}

	public float getFriction() {
		return friction;
	}
}
