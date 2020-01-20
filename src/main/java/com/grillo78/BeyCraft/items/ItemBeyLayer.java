package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.abilities.Ability;
import com.grillo78.BeyCraft.inventory.BeyContainer;
import com.grillo78.BeyCraft.inventory.ItemBeyProvider;
import com.grillo78.BeyCraft.items.render.BeyItemStackRendererTileEntity;
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

public class ItemBeyLayer extends ItemBeyPart {

	protected final float rotationDirection;
	private final int attack;
	private final int defense;
	private final int weight;
	private final int burst;

	public ItemBeyLayer(String name, int rotationDirection, int attack, int defense, int weight,
			int burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
		super(name, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTLAYERS,new Item.Properties().setTEISR(()->()-> new BeyItemStackRendererTileEntity()));
		this.attack = attack;
		this.defense = defense;
		this.weight = weight;
		this.burst = burst;
		this.rotationDirection = rotationDirection;
		BeyRegistry.ITEMSLAYER.add(this);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		
		return new ItemBeyProvider();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		if (!world.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player,
					new SimpleNamedContainerProvider(
							(id, playerInventory, playerEntity) -> new BeyContainer(BeyRegistry.BEY_CONTAINER, id,
									player.getHeldItem(handIn), playerInventory, playerEntity),
							new StringTextComponent(getRegistryName().getPath())));
		}
		return super.onItemRightClick(world, player, handIn);
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getWeight() {
		return weight;
	}

	public int getBurst() {
		return burst;
	}

	public float getRotationDirection() {
		return rotationDirection;
	}

}
