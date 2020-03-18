package com.grillo78.beycraft.items;

import java.util.Random;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;

public class ItemBeyPackage extends Item {
	public ItemBeyPackage(String name) {
		super(new Item.Properties().group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
		this.setRegistryName(new ResourceLocation(Reference.MODID, name));
		BeyRegistry.ITEMS.add(this);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote) {
			BeyCraft.logger.info(BeyRegistry.ITEMSLAYER.size());
			Random random = new Random();
			int randomNumber = random.nextInt(BeyRegistry.ITEMSLAYER.size());
			if (randomNumber != 0) {
				ItemEntity itemLayer = new ItemEntity(context.getWorld(), context.getPlayer().getPosition().getX(),
						context.getPlayer().getPosition().getY(), context.getPlayer().getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSLAYER.get(randomNumber - 1), 1));
				context.getWorld().addEntity(itemLayer);
			} else {
				ItemEntity itemLayer = new ItemEntity(context.getWorld(), context.getPlayer().getPosition().getX(),
						context.getPlayer().getPosition().getY(), context.getPlayer().getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSLAYER.get(randomNumber), 1));
				context.getWorld().addEntity(itemLayer);
			}
			randomNumber = random.nextInt(BeyRegistry.ITEMSLAYER.size());
			if (randomNumber != 0) {
				ItemEntity itemDisk = new ItemEntity(context.getWorld(), context.getPlayer().getPosition().getX(),
						context.getPlayer().getPosition().getY(), context.getPlayer().getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSDISKLIST.get(randomNumber - 1), 1));
				context.getWorld().addEntity(itemDisk);
			} else {
				ItemEntity itemDisk = new ItemEntity(context.getWorld(), context.getPlayer().getPosition().getX(),
						context.getPlayer().getPosition().getY(), context.getPlayer().getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSDISKLIST.get(randomNumber), 1));
				context.getWorld().addEntity(itemDisk);
			}
			randomNumber = random.nextInt(BeyRegistry.ITEMSLAYER.size());
			if (randomNumber != 0) {
				ItemEntity itemDriver = new ItemEntity(context.getWorld(), context.getPlayer().getPosition().getX(),
						context.getPlayer().getPosition().getY(), context.getPlayer().getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSDRIVER.get(randomNumber - 1), 1));
				context.getWorld().addEntity(itemDriver);
			} else {
				ItemEntity itemDriver = new ItemEntity(context.getWorld(), context.getPlayer().getPosition().getX(),
						context.getPlayer().getPosition().getY(), context.getPlayer().getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSDRIVER.get(randomNumber), 1));
				context.getWorld().addEntity(itemDriver);
			}
			context.getPlayer().getHeldItem(context.getHand()).shrink(1);
		}
		return super.onItemUse(context);
	}
}
