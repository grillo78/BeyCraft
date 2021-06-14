package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;

import com.grillo78.beycraft.items.render.GenericPartItemStackRendererTileEntity;
import com.grillo78.beycraft.util.BeyTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBeyFrame extends ItemBeyPart{

	private float attack;
	private float defense;

	public ItemBeyFrame(String name, float attack, float defense, BeyTypes type) {
		super(name, type, null, null, BeyCraft.BEYCRAFTDISKS,new Item.Properties().setISTER(()->GenericPartItemStackRendererTileEntity::new));
		BeyRegistry.ITEMSFRAME.add(this);
	}

	public float getAttack() {
		return attack;
	}

	public float getDefense() {
		return defense;
	}
}
