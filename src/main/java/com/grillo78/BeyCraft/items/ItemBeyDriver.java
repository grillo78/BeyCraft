package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBeyDriver extends Item implements IHasModel{
	
	public float height;
	public float friction;
	public float radiusReducion;
	
	public ItemBeyDriver(String name, float height, float friction, float radiusReduction) {
		setCreativeTab(BeyCraft.BEYCRAFTDRIVERS);
		setRegistryName(name);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		this.height = height;
		this.radiusReducion = radiusReduction;
		this.friction = friction;
		BeyRegistry.ITEMSDRIVER.add(this);
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(),"inventory"));
	}
}
