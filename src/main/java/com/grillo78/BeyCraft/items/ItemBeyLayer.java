package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.abilities.IAbility;
import com.grillo78.BeyCraft.util.BeyTypes;

public class ItemBeyLayer extends ItemBeyPart {

	public final float height;
	protected final float rotationDirection;
	private final int attack;
	private final int defense;
	private final int weight;
	private final int burst;

	public ItemBeyLayer(String name, float height, int rotationDirection, int attack, int defense, int weight,
			int burst, IAbility primaryAbility, IAbility secundaryAbility, BeyTypes type) {
		super(name, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTLAYERS);
		this.attack = attack;
		this.defense = defense;
		this.weight = weight;
		this.burst = burst;
		this.height = height;
		this.rotationDirection = rotationDirection;
		BeyRegistry.ITEMSLAYER.add(this);
	}

	public float getHeight() {
		return height;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getWeight() {
		return weight;
	}

	public int getBurst() {
		return burst;
	}

	public float getRotationDirection() {
		return rotationDirection;
	}

//	public boolean canAbsorb(Entity entity) {
//		return (PRIMARYABILITY instanceof Absorb || SECUNDARYABILITY instanceof Absorb)
//				&& entity.getRotationDirection() != rotationDirection;
//	}

}
