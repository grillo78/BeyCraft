package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.capabilities.IBladerLevel;
import com.grillo78.BeyCraft.capabilities.Provider;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.inventory.BeyBladeProvider;
import com.grillo78.BeyCraft.network.BladerLevelMessage;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
		setCreativeTab(BeyCraft.BEYCRAFTTAB);
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
								.getStackInSlot(2).getItem() instanceof ItemBeyDriver
						&& !worldIn.isRemote) {
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
					beyEntity.setLocationAndAngles(playerIn.posX + playerIn.getLookVec().x, playerIn.posY + 1,
							playerIn.posZ + playerIn.getLookVec().z, playerIn.rotationYaw - 30 * rotation, 0);
					beyEntity.rotationYawHead = beyEntity.rotationYaw;
					beyEntity.renderYawOffset = beyEntity.rotationYaw;
					worldIn.spawnEntity(beyEntity);
					if (((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
							.getExperience() != ((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP,
									EnumFacing.UP)).getMaxExperience()) {
						((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP)).setExperience(
								((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
										.getExperience() + 0.1F);
					} else {
						((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP)).setBladerLevel(
								((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
										.getBladerLevel() + 1);
						((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
								.setExperience(0);
					}
					BeyCraft.INSTANCE.sendTo(
							new BladerLevelMessage(
									(int) playerIn.getCapability(Provider.BLADERLEVEL_CAP, null).getBladerLevel()),
							(EntityPlayerMP) playerIn);
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
