package com.grillo78.beycraft.items;

import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.util.BeyTypes;

public class ItemBeyLayerDual extends ItemBeyLayer {

	public ItemBeyLayerDual(String name, float height, int attack, int defense, int weight, int burst,
			Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
		super(name, 1, attack, defense, weight, burst, primaryAbility, secundaryAbility, type);
	}

}
