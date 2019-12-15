package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.abilities.IAbility;
import com.grillo78.BeyCraft.util.BeyTypes;

public class ItemBeyLayerDual extends ItemBeyLayer {

	public ItemBeyLayerDual(String name, float height, int attack, int defense, int weight, int burst,
			IAbility primaryAbility, IAbility secundaryAbility, BeyTypes type) {
		super(name, height, 1, attack, defense, weight, burst, primaryAbility, secundaryAbility, type);
	}

}
