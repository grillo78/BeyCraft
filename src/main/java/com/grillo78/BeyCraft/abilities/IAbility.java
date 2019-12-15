package com.grillo78.BeyCraft.abilities;

import com.grillo78.BeyCraft.entity.EntityBey;

import net.minecraft.item.ItemStack;

/**
 * @author a19guillermong
 *
 */
public interface IAbility {

	
	public void executeAbility(EntityBey entity);

	public void executeAbility(ItemStack driver);
}
