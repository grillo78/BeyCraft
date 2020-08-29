package com.grillo78.beycraft.items;

import java.util.List;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.abilities.MultiMode;
import com.grillo78.beycraft.abilities.MultiType;
import com.grillo78.beycraft.util.BeyTypes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemBeyPart extends Item {

	protected BeyTypes type;
	protected final Ability PRIMARYABILITY;
	protected final Ability SECUNDARYABILITY;

	public ItemBeyPart(String name, BeyTypes type, Ability primaryAbility, Ability secundaryAbility, ItemGroup tab,
			Item.Properties properties) {
		super(properties.group(tab).maxStackSize(1));
		PRIMARYABILITY = primaryAbility;
		SECUNDARYABILITY = secundaryAbility;
		this.type = type;
		setRegistryName(new ResourceLocation(Reference.MODID, name));
	}

	public Ability getPrimaryAbility() {
		return PRIMARYABILITY;
	}

	public Ability getSecundaryAbility() {
		return SECUNDARYABILITY;
	}

	public BeyTypes getType(ItemStack stack) {
		if (PRIMARYABILITY instanceof MultiType) {
			if (stack.hasTag() && stack.getTag().contains("Type")) {
				return ((MultiType) PRIMARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getType();
			}
			return ((MultiType) PRIMARYABILITY).getTypes().get(0).getType();
		}
		if (SECUNDARYABILITY instanceof MultiType) {
			if (stack.hasTag() && stack.getTag().contains("Type")) {
				return ((MultiType) SECUNDARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getType();
			}
			return ((MultiType) SECUNDARYABILITY).getTypes().get(0).getType();
		}
		return type;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		if (player.isCrouching() && (PRIMARYABILITY instanceof MultiType || SECUNDARYABILITY instanceof MultiType)) {
			if (PRIMARYABILITY instanceof MultiType) {
				PRIMARYABILITY.executeAbility(player.getHeldItem(handIn));
			} else {
				SECUNDARYABILITY.executeAbility(player.getHeldItem(handIn));
			}
			return super.onItemRightClick(world, player, handIn);
		}
		return ActionResult.resultFail(player.getHeldItem(handIn));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		BeyTypes type = getType(stack);
		if(type!=null) tooltip.add(new TranslationTextComponent(type.getName()));
		if (getPrimaryAbility() instanceof MultiMode) {
			if (stack.hasTag() && stack.getTag().contains("mode")) {
				tooltip.add(new StringTextComponent(stack.getTag().getString("mode")));
			}else{
				tooltip.add(new StringTextComponent(((MultiMode)getPrimaryAbility()).getModes().get(0).getName()));
			}
		}
		if (getSecundaryAbility() instanceof MultiMode) {
			if (stack.hasTag() && stack.getTag().contains("mode")) {
				tooltip.add(new StringTextComponent(stack.getTag().getString("mode")));
			}else{
				tooltip.add(new StringTextComponent(((MultiMode)getSecundaryAbility()).getModes().get(0).getName()));
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
