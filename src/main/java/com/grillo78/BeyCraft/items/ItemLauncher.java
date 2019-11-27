package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.capabilities.IBladerLevel;
import com.grillo78.BeyCraft.capabilities.Provider;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.inventory.BeyBladeProvider;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemLauncher extends Item implements IHasModel {

	private int rotation;

	public ItemLauncher(String name, int rotation) {
		setCreativeTab(BeyCraft.beyCraftTab);
		setRegistryName(name);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		this.rotation = rotation;
		BeyRegistry.ITEMS.add(this);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if (stack.getItem() == this) {
			return new BeyBladeProvider();
		}
		return null;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			if (playerIn.isSneaking()) {
				playerIn.openGui(BeyCraft.instance, 0, worldIn, 0, 0, 0);
			} else {
				if (playerIn.getHeldItem(handIn)
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP).getStackInSlot(0)
						.getItem() instanceof ItemBeyLayer
						&& playerIn.getHeldItem(handIn)
								.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
								.getStackInSlot(1).getItem() instanceof ItemBeyDisk
						&& playerIn.getHeldItem(handIn)
								.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
								.getStackInSlot(2).getItem() instanceof ItemBeyDriver) {
					EntityBey beyEntity = new EntityBey(worldIn,
							playerIn.getHeldItem(handIn)
									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
									.getStackInSlot(0),
							playerIn.getHeldItem(handIn)
									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
									.getStackInSlot(1),
							playerIn.getHeldItem(handIn)
									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
									.getStackInSlot(2),
							((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
									.getBladerLevel(),
							rotation, playerIn.getName());
					beyEntity.setLocationAndAngles(playerIn.posX + playerIn.getLookVec().x, playerIn.posY,
							playerIn.posZ + playerIn.getLookVec().z, playerIn.rotationYaw - 115 * rotation, 0);
//					beyEntity.setVelocity(playerIn.motionX, playerIn.motionY, playerIn.motionZ);
					BeyCraft.logger.info(playerIn.rotationYaw);
					worldIn.spawnEntity(beyEntity);
					((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
					.setBladerLevel(((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
									.getBladerLevel()+0.01F);
					playerIn.getHeldItem(handIn)
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
							.getStackInSlot(0).shrink(1);
					playerIn.getHeldItem(handIn)
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
							.getStackInSlot(1).shrink(1);
					playerIn.getHeldItem(handIn)
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
							.getStackInSlot(2).shrink(1);
				}
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public int getRotation() {
		return rotation;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0,
				new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}
}
