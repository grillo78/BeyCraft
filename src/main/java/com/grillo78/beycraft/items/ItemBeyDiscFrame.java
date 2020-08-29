package com.grillo78.beycraft.items;

import javax.annotation.Nullable;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.inventory.BeyDiscFrameContainer;
import com.grillo78.beycraft.inventory.ItemBeyDiscFrameProvider;
import com.grillo78.beycraft.items.render.DiscFrameItemStackRendererTileEntity;

import com.grillo78.beycraft.util.BeyTypes;
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
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemBeyDiscFrame extends ItemBeyDisc {

	private float frameRotation;

	public ItemBeyDiscFrame(String name, float attack, float defense, float weight, float frameRotation, Ability primaryAbility,
							Ability secundaryAbility, BeyTypes type) {
		super(name, attack, defense, weight, primaryAbility, secundaryAbility, type,
				new Item.Properties().setISTER(() -> DiscFrameItemStackRendererTileEntity::new));
		this.frameRotation = frameRotation;
		BeyRegistry.ITEMSDISCFRAME.add(this);
	}

	public float getFrameRotation() {
		return frameRotation;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new ItemBeyDiscFrameProvider();
	}

	@Override
	public float getAttack(ItemStack stack) {
		return super.getAttack(stack);
	}

	@Override
	public float getDefense(ItemStack stack) {
		float[] attack = {super.getDefense(stack)};
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
			if(h.getStackInSlot(0).getItem() instanceof ItemBeyFrame){
				attack[0] += ((ItemBeyFrame) h.getStackInSlot(0).getItem()).getAttack();
			}
		});
		return attack[0];
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		if (!world.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player,
					new SimpleNamedContainerProvider(
							(id, playerInventory, playerEntity) -> new BeyDiscFrameContainer(id,
									player.getHeldItem(handIn), playerInventory, playerEntity, handIn),
							new StringTextComponent(getRegistryName().getPath())));
		}
		return super.onItemRightClick(world, player, handIn);
	}
}
