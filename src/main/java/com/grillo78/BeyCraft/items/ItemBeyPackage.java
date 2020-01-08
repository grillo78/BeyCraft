package com.grillo78.BeyCraft.items;

import java.util.Random;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.Reference;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;

public class ItemBeyPackage extends Item{
	public ItemBeyPackage(String name) {
		super(new Item.Properties().group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
		this.setRegistryName(new ResourceLocation(Reference.MODID, name));
		BeyRegistry.ITEMS.add(this);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if(!context.getWorld().isRemote) {
			ItemEntity itemLayer = new ItemEntity(context.getWorld(), context.getPlayer().serverPosX, context.getPlayer().serverPosY, context.getPlayer().serverPosZ, new ItemStack(BeyRegistry.ITEMSLAYER.get(new Random().nextInt(BeyRegistry.ITEMSLAYER.size()-1)), 1));
			context.getWorld().addEntity(itemLayer);
			ItemEntity itemDisk = new ItemEntity(context.getWorld(), context.getPlayer().serverPosX, context.getPlayer().serverPosY, context.getPlayer().serverPosZ, new ItemStack(BeyRegistry.ITEMSDISK.get(new Random().nextInt(BeyRegistry.ITEMSDISK.size()-1)), 1));
			context.getWorld().addEntity(itemDisk);
			ItemEntity itemDriver = new ItemEntity(context.getWorld(), context.getPlayer().serverPosX, context.getPlayer().serverPosY, context.getPlayer().serverPosZ, new ItemStack(BeyRegistry.ITEMSDRIVER.get(new Random().nextInt(BeyRegistry.ITEMSDRIVER.size()-1)), 1));
			itemDriver.setDefaultPickupDelay();
			context.getWorld().addEntity(itemDriver);
			context.getPlayer().getHeldItem(context.getHand()).shrink(1);
		}
		return super.onItemUse(context);
	}
}
