package com.grillo78.BeyCraft.items;

import java.util.List;

import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.abilities.Ability;
import com.grillo78.BeyCraft.items.render.DiskFrameItemStackRendererTileEntity;
import com.grillo78.BeyCraft.util.BeyTypes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
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

	public Ability getPrimaryAbilitys() {
		return PRIMARYABILITY;
	}

	public Ability getSecundaryAbility() {
		return SECUNDARYABILITY;
	}

	public BeyTypes getType() {
		return type;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getPlayer().isCrouching()) {
			if (PRIMARYABILITY != null) {
				PRIMARYABILITY.executeAbility(context.getPlayer().getHeldItem(context.getHand()));
			}
		} else {
			if (SECUNDARYABILITY != null) {
				SECUNDARYABILITY.executeAbility(context.getPlayer().getHeldItem(context.getHand()));
			}
		}
		return super.onItemUse(context);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (type != null) {
			if (stack.hasTag()) {
				tooltip.add(new TranslationTextComponent(stack.getTag().getString("Type")));
			} else {
				tooltip.add(new TranslationTextComponent(type.name()));
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
