package com.grillo78.beycraft.items;

import javax.annotation.Nullable;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.inventory.BeyDiscFrameContainer;
import com.grillo78.beycraft.inventory.ItemBeyDiscFrameProvider;
import com.grillo78.beycraft.items.render.DiskFrameItemStackRendererTileEntity;

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

public class ItemBeyDiscFrame extends ItemBeyDisc {

	public ItemBeyDiscFrame(String name, float attack, float defense, float weight, float burst, Ability primaryAbility,
							Ability secundaryAbility) {
		super(name, attack, defense, weight, burst, primaryAbility, secundaryAbility,
				new Item.Properties().setISTER(() -> DiskFrameItemStackRendererTileEntity::new));
		BeyRegistry.ITEMSDISCFRAME.add(this);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new ItemBeyDiscFrameProvider();
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
