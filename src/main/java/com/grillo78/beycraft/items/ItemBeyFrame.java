package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBeyFrame extends ItemBeyPart{

	private float attack;
	private float defense;

	public ItemBeyFrame(String name, float attack, float defense) {
		super(name, null, null, null, BeyCraft.BEYCRAFTDISKS,new Item.Properties());
		BeyRegistry.ITEMSFRAME.add(this);
	}

	public float getAttack() {
		return attack;
	}

	public float getDefense() {
		return defense;
	}
}
