package com.grillo78.BeyCraft.items;

import javax.annotation.Nullable;

import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.abilities.Ability;
import com.grillo78.BeyCraft.inventory.BeyDiskFrameContainer;
import com.grillo78.BeyCraft.inventory.ItemBeyDiskFrameProvider;
import com.grillo78.BeyCraft.items.render.DiskFrameItemStackRendererTileEntity;
import com.grillo78.BeyCraft.util.BeyTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemBeyDiskFrame extends ItemBeyDisk {

	public ItemBeyDiskFrame(String name, int attack, int defense, int weight, int burst, Ability primaryAbility,
			Ability secundaryAbility, BeyTypes type) {
		super(name, attack, defense, weight, burst, primaryAbility, secundaryAbility, type,
				new Item.Properties().setTEISR(() -> () -> new DiskFrameItemStackRendererTileEntity()));
		BeyRegistry.ITEMSDISKFRAME.add(this);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new ItemBeyDiskFrameProvider();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		if (!world.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player,
					new SimpleNamedContainerProvider(
							(id, playerInventory, playerEntity) -> new BeyDiskFrameContainer(BeyRegistry.DISK_FRAME_CONTAINER,
									id, player.getHeldItem(handIn), playerInventory, playerEntity),
							new StringTextComponent(getRegistryName().getPath())));
		}
		return super.onItemRightClick(world, player, handIn);
	}
}
