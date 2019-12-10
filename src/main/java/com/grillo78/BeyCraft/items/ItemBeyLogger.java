package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBeyLogger extends Item implements IHasModel{

	public ItemBeyLogger(String name) {
		setCreativeTab(BeyCraft.BEYCRAFTTAB);
		setRegistryName(new ResourceLocation(Reference.MODID,name));
		setUnlocalizedName(name);
		setMaxStackSize(1);
		BeyRegistry.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(),"inventory"));
	}
}
