package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBeyDisk extends Item implements IHasModel{

	public float height;
	
	public ItemBeyDisk(String name, float height) {
		this.setCreativeTab(BeyCraft.BEYCRAFTDISKS);
		setRegistryName(new ResourceLocation(Reference.MODID,name));
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		this.height = height;
		BeyRegistry.ITEMSDISK.add(this);
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(),"inventory"));
	}
}
