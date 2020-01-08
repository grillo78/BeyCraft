package com.grillo78.BeyCraft.items;

import javax.annotation.Nullable;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.inventory.ItemLauncherProvider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemLauncher extends Item {

	private int rotation;

	public ItemLauncher(String name, int rotation) {
		super(new Item.Properties().group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
		setRegistryName(new ResourceLocation(Reference.MODID, name));
		this.rotation = rotation;
		BeyRegistry.ITEMS.add(this);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (stack.getItem() != this) {
			return null;
		}
		return new ItemLauncherProvider();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		if (!player.isCrouching()) {
			if (!world.isRemote && BeyCraft.BEY_ENTITY_TYPE.get() != null) {
				EntityBey entity = new EntityBey(BeyCraft.BEY_ENTITY_TYPE.get(), world,
						new ItemStack(BeyRegistry.VALTRYEKV2), new ItemStack(BeyRegistry.BOOSTDISK), new ItemStack(BeyRegistry.EVOLUTIONDRIVER));
				entity.setPositionAndRotation(player.getPosition().getX() + player.getLookVec().x, player.getPosition().getY() + 1,
						player.getPosition().getZ() + player.getLookVec().z,0,0);
				BeyCraft.logger.info(entity.getPosition().toString());
				world.addEntity(entity);
				BeyCraft.logger.info("succes");
			}
		}else {
			
		}
		return super.onItemRightClick(world, player, handIn);
	}

//	@Override
//	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
//		if (!worldIn.isRemote) {
//			if (playerIn.isSneaking()) {
//				playerIn.openGui(BeyCraft.instance, 0, worldIn, 0, 0, 0);
//			} else {
//				if (playerIn.getHeldItem(handIn)
//						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP).getStackInSlot(0)
//						.getItem() instanceof ItemBeyLayer
//						&& playerIn.getHeldItem(handIn)
//								.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//								.getStackInSlot(1).getItem() instanceof ItemBeyDisk
//						&& playerIn.getHeldItem(handIn)
//								.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//								.getStackInSlot(2).getItem() instanceof ItemBeyDriver
//						&& !worldIn.isRemote) {
//					EntityBey beyEntity = new EntityBey(worldIn,
//							playerIn.getHeldItem(handIn)
//									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//									.getStackInSlot(0),
//							playerIn.getHeldItem(handIn)
//									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//									.getStackInSlot(1),
//							playerIn.getHeldItem(handIn)
//									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//									.getStackInSlot(2),
//							((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
//									.getBladerLevel(),
//							rotation, playerIn.getName());
//					beyEntity.setLocationAndAngles(playerIn.posX + playerIn.getLookVec().x, playerIn.posY + 1,
//							playerIn.posZ + playerIn.getLookVec().z, playerIn.rotationYaw, 0);
//					beyEntity.rotationYawHead = beyEntity.rotationYaw;
//					beyEntity.renderYawOffset = beyEntity.rotationYaw;
//					worldIn.spawnEntity(beyEntity);
//					if (((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
//							.getExperience() != ((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP,
//									EnumFacing.UP)).getMaxExperience()) {
//						((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP)).setExperience(
//								((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
//										.getExperience() + 0.1F);
//					} else {
//						((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP)).setBladerLevel(
//								((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
//										.getBladerLevel() + 1);
//						((IBladerLevel) playerIn.getCapability(Provider.BLADERLEVEL_CAP, EnumFacing.UP))
//								.setExperience(0);
//					}
//					BeyCraft.INSTANCE.sendTo(
//							new BladerLevelMessage(
//									(int) playerIn.getCapability(Provider.BLADERLEVEL_CAP, null).getBladerLevel()),
//							(EntityPlayerMP) playerIn);
//					playerIn.getHeldItem(handIn)
//							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//							.getStackInSlot(0).shrink(1);
//					playerIn.getHeldItem(handIn)
//							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//							.getStackInSlot(1).shrink(1);
//					playerIn.getHeldItem(handIn)
//							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//							.getStackInSlot(2).shrink(1);
//				}
//			}
//		}
//		return super.onItemRightClick(worldIn, playerIn, handIn);
//	}

	public int getRotation() {
		return rotation;
	}
}
