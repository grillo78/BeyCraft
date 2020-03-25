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
		super(new Item.Properties().maxStackSize(1).group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
		setRegistryName(new ResourceLocation(Reference.MODID, name));
		BeyRegistry.ITEMS.put(name,this);
	}

}
