package com.grillo78.beycraft.items;

import java.util.List;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.abilities.MultiType;
import com.grillo78.beycraft.util.BeyTypes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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

    public Ability getPrimaryAbility() {
        return PRIMARYABILITY;
    }

    public Ability getSecundaryAbility() {
        return SECUNDARYABILITY;
    }

    public BeyTypes getType() {
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
        if (type != null) {
            if (PRIMARYABILITY instanceof MultiType || SECUNDARYABILITY instanceof MultiType) {
                if (stack.hasTag()) {
                    tooltip.add(new TranslationTextComponent(stack.getTag().getString("Type")));
                } else {
                    if (PRIMARYABILITY instanceof MultiType) {
						tooltip.add(new TranslationTextComponent(((MultiType) PRIMARYABILITY).getTypes().get(0).name()));
                    } else {
						tooltip.add(new TranslationTextComponent(((MultiType) SECUNDARYABILITY).getTypes().get(0).name()));
                    }
                }
            } else {
                tooltip.add(new TranslationTextComponent(type.name()));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
