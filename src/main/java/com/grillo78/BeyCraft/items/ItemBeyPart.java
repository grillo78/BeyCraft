package com.grillo78.BeyCraft.items;

import java.util.List;

import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.abilities.IAbility;
import com.grillo78.BeyCraft.util.BeyTypes;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBeyPart extends Item implements IHasModel {

	protected BeyTypes type;
	protected final IAbility PRIMARYABILITY;
	protected final IAbility SECUNDARYABILITY;

	public ItemBeyPart(String name, BeyTypes type, IAbility primaryAbility,
			IAbility secundaryAbility,CreativeTabs tab) {
		PRIMARYABILITY = primaryAbility;
		SECUNDARYABILITY = secundaryAbility;
		this.type = type;
		setCreativeTab(tab);
		setRegistryName(new ResourceLocation(Reference.MODID, name));
		setUnlocalizedName(name);
		setMaxStackSize(1);
	}

	public IAbility getPrimaryAbilitys() {
		return PRIMARYABILITY;
	}

	public IAbility getSecundaryAbility() {
		return SECUNDARYABILITY;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0,
				new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}

	public BeyTypes getType() {
		return type;
	}


	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			if(PRIMARYABILITY!=null) {
				PRIMARYABILITY.executeAbility(player.getHeldItem(hand));
			}
		} else {
			if(SECUNDARYABILITY!=null) {
				SECUNDARYABILITY.executeAbility(player.getHeldItem(hand));
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			tooltip.add(stack.getTagCompound().getString("Type"));
		} else {
			tooltip.add(type.name());
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
