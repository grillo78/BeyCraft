package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBeyLayer extends Item implements IHasModel {

	public float height;
	protected float rotationDirection;
	private boolean canAbsorb;

	public ItemBeyLayer(String name, float height, int rotationDirection, boolean canAbsorb) {
		setCreativeTab(BeyCraft.beyCraftTab);
		setRegistryName(name);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		this.canAbsorb = canAbsorb;
		this.height = height;
		this.rotationDirection = rotationDirection;
		BeyRegistry.ITEMSLAYER.add(this);
	}

	public float getRotationDirection() {
		return rotationDirection;
	}

	public boolean canAbsorb(EntityBey entity) {
		return canAbsorb && entity.getRotationDirection() != rotationDirection;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0,
				new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}
	
	
}
