package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.abilities.Ability;
import com.grillo78.BeyCraft.util.BeyTypes;

import net.minecraft.item.Item;

public class ItemBeyLayer extends ItemBeyPart {

	protected final float rotationDirection;
	private final int attack;
	private final int defense;
	private final int weight;
	private final int burst;

	public ItemBeyLayer(String name, int rotationDirection, int attack, int defense, int weight,
			int burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
		super(name, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTLAYERS,new Item.Properties());
		this.attack = attack;
		this.defense = defense;
		this.weight = weight;
		this.burst = burst;
		this.rotationDirection = rotationDirection;
		BeyRegistry.ITEMSLAYER.add(this);
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

}
