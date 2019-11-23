package com.grillo78.BeyCraft.items;

import java.util.Random;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBeyPackage extends Item implements IHasModel{
	public ItemBeyPackage(String name) {
		this.setCreativeTab(BeyCraft.beyCraftTab);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		BeyRegistry.ITEMS.add(this);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote)
		{

			EntityItem itemLayer = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, new ItemStack(BeyRegistry.ITEMSLAYER.get(new Random().nextInt(BeyRegistry.ITEMSLAYER.size()-1)), 1));
			worldIn.spawnEntity(itemLayer);
			EntityItem itemDisk = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, new ItemStack(BeyRegistry.ITEMSDISK.get(new Random().nextInt(BeyRegistry.ITEMSDISK.size()-1)), 1));
			worldIn.spawnEntity(itemDisk);
			EntityItem itemDriver = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, new ItemStack(BeyRegistry.ITEMSDRIVER.get(new Random().nextInt(BeyRegistry.ITEMSDRIVER.size()-1)), 1));
			worldIn.spawnEntity(itemDriver);

		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(),"inventory"));
	}
}
