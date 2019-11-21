package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBeyDriver extends Item implements IHasModel{
	
	public float height;
	public ItemBeyDriver(String name, float height) {
		setCreativeTab(BeyCraft.beyCraftTab);
		setRegistryName(name);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		this.height = height;
		BeyRegistry.ITEMSDRIVER.add(this);
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(),"inventory"));
	}
}
