package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBeyDisk extends Item implements IHasModel{

	public ItemBeyDisk(String name) {
		this.setCreativeTab(BeyCraft.beyCraftTab);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		BeyRegistry.ITEMSDISK.add(this);
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(),"inventory"));
	}
}
