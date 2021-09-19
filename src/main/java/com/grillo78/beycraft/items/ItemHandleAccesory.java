package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * @author a19guillermong
 *
 */
public class ItemHandleAccesory extends Item{

	/**
	 * @param properties
	 */
	public ItemHandleAccesory(String name) {
		super(new Item.Properties().stacksTo(1).tab(BeyCraft.BEYCRAFTTAB).stacksTo(1));
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		BeyRegistry.ITEMS.put(name,this);
	}

}
