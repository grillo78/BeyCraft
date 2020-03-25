package com.grillo78.beycraft.items;

import java.util.Random;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemBeyPackage extends Item {
	public ItemBeyPackage(String name) {
		super(new Item.Properties().group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
		this.setRegistryName(new ResourceLocation(Reference.MODID, name));
		BeyRegistry.ITEMS.put(name,this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isRemote) {
			BeyCraft.logger.info(BeyRegistry.ITEMSLAYER.size());
			Random random = new Random();
			int randomNumber = random.nextInt(BeyRegistry.ITEMSLAYER.size());
			if (randomNumber != 0) {
				ItemEntity itemLayer = new ItemEntity(worldIn, playerIn.getPosition().getX(),
						playerIn.getPosition().getY(), playerIn.getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSLAYER.get(randomNumber - 1), 1));
				worldIn.addEntity(itemLayer);
			} else {
				ItemEntity itemLayer = new ItemEntity(worldIn, playerIn.getPosition().getX(),
						playerIn.getPosition().getY(), playerIn.getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSLAYER.get(randomNumber), 1));
				worldIn.addEntity(itemLayer);
			}
			randomNumber = random.nextInt(BeyRegistry.ITEMSLAYER.size());
			if (randomNumber != 0) {
				ItemEntity itemDisk = new ItemEntity(worldIn, playerIn.getPosition().getX(),
						playerIn.getPosition().getY(), playerIn.getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSDISCLIST.get(randomNumber - 1), 1));
				worldIn.addEntity(itemDisk);
			} else {
				ItemEntity itemDisk = new ItemEntity(worldIn, playerIn.getPosition().getX(),
						playerIn.getPosition().getY(), playerIn.getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSDISCLIST.get(randomNumber), 1));
				worldIn.addEntity(itemDisk);
			}
			randomNumber = random.nextInt(BeyRegistry.ITEMSLAYER.size());
			if (randomNumber != 0) {
				ItemEntity itemDriver = new ItemEntity(worldIn, playerIn.getPosition().getX(),
						playerIn.getPosition().getY(), playerIn.getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSDRIVER.get(randomNumber - 1), 1));
				worldIn.addEntity(itemDriver);
			} else {
				ItemEntity itemDriver = new ItemEntity(worldIn, playerIn.getPosition().getX(),
						playerIn.getPosition().getY(), playerIn.getPosition().getZ(),
						new ItemStack(BeyRegistry.ITEMSDRIVER.get(randomNumber), 1));
				worldIn.addEntity(itemDriver);
			}
			playerIn.getHeldItem(handIn).shrink(1);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}


}
