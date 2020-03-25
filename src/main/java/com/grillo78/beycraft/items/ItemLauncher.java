package com.grillo78.beycraft.items;

import javax.annotation.Nullable;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.inventory.ItemLauncherProvider;
import com.grillo78.beycraft.inventory.LauncherContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemLauncher extends Item {

	private int rotation;

	public ItemLauncher(String name, int rotation) {
		super(new Item.Properties().group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
		setRegistryName(new ResourceLocation(Reference.MODID, name));
		this.rotation = rotation;
		BeyRegistry.ITEMS.put(name,this);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new ItemLauncherProvider();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		if (!player.isCrouching()) {
			if (!world.isRemote && BeyRegistry.BEY_ENTITY_TYPE != null) {
				ItemStack launcher = player.getHeldItem(handIn);
				launcher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
					if (h.getStackInSlot(0).getItem() instanceof ItemBeyLayer) {
						EntityBey entity = new EntityBey(BeyRegistry.BEY_ENTITY_TYPE, world,
								h.getStackInSlot(0).copy(),
								rotation);
						entity.setLocationAndAngles(player.getPositionVec().x + player.getLookVec().x,
								player.getPositionVec().y + 1 + player.getLookVec().y,
								player.getPositionVec().z + player.getLookVec().z, player.rotationYaw, 0);
						entity.rotationYawHead = entity.rotationYaw;
						entity.renderYawOffset = entity.rotationYaw;
						world.addEntity(entity);
						BeyCraft.logger.info(new Vec3d((player.getPositionVec().x - player.prevPosX) * 2,
								(player.getPositionVec().y - player.prevPosY) * 2,
								(player.getPositionVec().z - player.prevPosZ) * 2).toString());
						entity.addVelocity((player.getPositionVec().x - player.prevPosX) * 2,
								(player.getPositionVec().y - player.prevPosY) * 2,
								(player.getPositionVec().z - player.prevPosZ) * 2);
						h.getStackInSlot(0).shrink(1);
						BeyCraft.logger.info("succes");
					}
				});
			}
		} else {
			if (!world.isRemote) {
				NetworkHooks.openGui((ServerPlayerEntity) player,
						new SimpleNamedContainerProvider(
								(id, playerventory, playerEntity) -> new LauncherContainer(BeyRegistry.LAUNCHER_CONTAINER,
										id, player.getHeldItem(handIn), playerventory, playerEntity, rotation, handIn),
								new StringTextComponent(getRegistryName().getPath())));
			}
		}
		return super.onItemRightClick(world, player, handIn);
	}

//	@Override
//	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
//		if (!worldIn.isRemote) {
//			if (player.isSneaking()) {
//				player.openGui(BeyCraft.instance, 0, worldIn, 0, 0, 0);
//			} else {
//				if (player.getHeldItem(handIn)
//						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).getStackInSlot(0)
//						.getItem() instanceof ItemBeyLayer
//						&& player.getHeldItem(handIn)
//								.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//								.getStackInSlot(1).getItem() instanceof ItemBeyDisk
//						&& player.getHeldItem(handIn)
//								.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//								.getStackInSlot(2).getItem() instanceof ItemBeyDriver
//						&& !worldIn.isRemote) {
//					EntityBey entity = new EntityBey(worldIn,
//							player.getHeldItem(handIn)
//									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//									.getStackInSlot(0),
//							player.getHeldItem(handIn)
//									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//									.getStackInSlot(1),
//							player.getHeldItem(handIn)
//									.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//									.getStackInSlot(2),
//							((IBladerLevel) player.getCapability(Provider.BLADERLEVEL_CAP))
//									.getBladerLevel(),
//							rotation, player.getName());
//					entity.setLocationAndAngles(player.posX + player.getLookVec().x, player.posY + 1,
//							player.posZ + player.getLookVec().z, player.rotationYaw, 0);
//					entity.rotationYawHead = entity.rotationYaw;
//					entity.renderYawOffset = entity.rotationYaw;
//					worldIn.spawnEntity(entity);
//					if (((IBladerLevel) player.getCapability(Provider.BLADERLEVEL_CAP))
//							.getExperience() != ((IBladerLevel) player.getCapability(Provider.BLADERLEVEL_CAP,
//									EnumFacing.UP)).getMaxExperience()) {
//						((IBladerLevel) player.getCapability(Provider.BLADERLEVEL_CAP)).setExperience(
//								((IBladerLevel) player.getCapability(Provider.BLADERLEVEL_CAP))
//										.getExperience() + 0.1F);
//					} else {
//						((IBladerLevel) player.getCapability(Provider.BLADERLEVEL_CAP)).setBladerLevel(
//								((IBladerLevel) player.getCapability(Provider.BLADERLEVEL_CAP))
//										.getBladerLevel() + 1);
//						((IBladerLevel) player.getCapability(Provider.BLADERLEVEL_CAP))
//								.setExperience(0);
//					}
//					BeyCraft.INSTANCE.sendTo(
//							new BladerLevelMessage(
//									(int) player.getCapability(Provider.BLADERLEVEL_CAP, null).getBladerLevel()),
//							(EntityPlayerMP) player);
//					player.getHeldItem(handIn)
//							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//							.getStackInSlot(0).shrink(1);
//					player.getHeldItem(handIn)
//							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//							.getStackInSlot(1).shrink(1);
//					player.getHeldItem(handIn)
//							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//							.getStackInSlot(2).shrink(1);
//				}
//			}
//		}
//		return super.onItemRightClick(worldIn, player, handIn);
//	}

	public int getRotation() {
		return rotation;
	}
}
