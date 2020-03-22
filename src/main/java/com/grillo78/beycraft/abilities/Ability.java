package com.grillo78.beycraft.abilities;

import com.grillo78.beycraft.entity.EntityBey;

import net.minecraft.item.ItemStack;

/**
 * @author a19guillermong
 *
 */
public abstract class Ability {

	
	public abstract void executeAbility(EntityBey entity);

	public abstract void executeAbility(ItemStack piece);
}
