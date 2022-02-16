package ga.beycraft.items;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.util.BeyTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemBeyLayerGT extends ItemBeyLayer {
    public ItemBeyLayerGT(String name, String displayName, int rotationDirection, float attack, float defense, float weight, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, displayName, rotationDirection, attack, defense, weight, 0, primaryAbility, secundaryAbility, type, null, null, null);
        BeyCraftRegistry.ITEMSLAYERGT.add(this);
    }

    @Override
    public boolean isBeyAssembled(ItemStack stack) {
        return super.isBeyAssembled(stack) && ItemStack.of((CompoundNBT) stack.getTag().get("chip")).getItem() instanceof ItemBeyGTChip;
    }

    @Override
    public float getWeight(ItemStack stack) {
        return super.getWeight(stack)+((ItemBeyGTChip)ItemStack.of(stack.getTag().getCompound("chip")).getItem()).getWeight();
    }

    @Override
    public float getBurst(ItemStack stack) {
        return ((ItemBeyGTChip)ItemStack.of(stack.getTag().getCompound("chip")).getItem()).getBurst();
    }

    @Override
    public Color getResonanceColor(ItemStack stack) {
        return ((ItemBeyGTChip)ItemStack.of(stack.getTag().getCompound("chip")).getItem()).getResonanceColor();
    }

    @Override
    public Color getSecondResonanceColor(ItemStack stack) {
        return ((ItemBeyGTChip)ItemStack.of(stack.getTag().getCompound("chip")).getItem()).getSecondResonanceColor();
    }

    @Override
    public Color getThirdResonanceColor(ItemStack stack) {
        return ((ItemBeyGTChip)ItemStack.of(stack.getTag().getCompound("chip")).getItem()).getThirdResonanceColor();
    }
}
