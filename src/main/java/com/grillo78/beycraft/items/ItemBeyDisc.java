package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;

import com.grillo78.beycraft.items.render.GenericPartItemStackRendererTileEntity;
import com.grillo78.beycraft.util.BeyTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBeyDisc extends ItemBeyPart{

	private float weight;
	private float attack;
	private float defense;

	public ItemBeyDisc(String name, float attack, float defense, float weight, Ability primaryAbility, Ability secondaryAbility, BeyTypes type, Item.Properties properties) {
		super(name, type, primaryAbility, secondaryAbility, BeyCraft.BEYCRAFTDISKS,properties.setISTER(() -> GenericPartItemStackRendererTileEntity::new));
		this.weight = weight;
		this.attack = attack;
		this.defense = defense;
		BeyRegistry.ITEMSDISCLIST.add(this);
	}

	public float getAttack(ItemStack stack) {
		return attack;
	}

	public float getDefense(ItemStack stack) {
		return defense;
	}

	public float getWeight() {
		return weight;
	}
}
