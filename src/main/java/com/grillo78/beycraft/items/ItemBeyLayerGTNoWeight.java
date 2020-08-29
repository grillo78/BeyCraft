package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.util.BeyTypes;

import net.minecraft.item.ItemStack;

public class ItemBeyLayerGTNoWeight extends ItemBeyLayer {
    public ItemBeyLayerGTNoWeight(String name, float rotationDirection, float attack, float defense, float weight, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, rotationDirection, attack, defense, weight, 0, primaryAbility, secundaryAbility, type);
        BeyRegistry.ITEMSLAYERGTNOWEIGHT.add(this);
    }

    @Override
    public float getWeight(ItemStack stack) {
        return super.getWeight(stack)+((ItemBeyGTChip)ItemStack.read(stack.getTag().getCompound("chip")).getItem()).getWeight();
    }

    @Override
    public float getBurst(ItemStack stack) {
        return ((ItemBeyGTChip)ItemStack.read(stack.getTag().getCompound("chip")).getItem()).getBurst();
    }
}
