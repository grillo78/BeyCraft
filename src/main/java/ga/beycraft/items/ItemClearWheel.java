package ga.beycraft.items;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.util.BeyTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemClearWheel extends ItemBeyLayer{
    public ItemClearWheel(String name, String displayName, int rotationDirection, float weight, float burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, rotationDirection, 0,0, weight, burst, primaryAbility, secundaryAbility, type, resonanceColor, secondResonanceColor, thirdResonanceColor);
        BeyCraftRegistry.ITEMSCLEARWHEEL.add(this);
    }

    @Override
    public boolean isBeyAssembled(ItemStack stack) {
        return super.isBeyAssembled(stack) && ItemStack.of((CompoundNBT) stack.getTag().get("FusionWheel")).getItem() instanceof ItemFusionWheel;
    }

    @Override
    public float getWeight(ItemStack stack) {
        return super.getWeight(stack)+((ItemFusionWheel)ItemStack.of(stack.getTag().getCompound("FusionWheel")).getItem()).getWeight();
    }

    @Override
    public float getAttack(ItemStack stack) {
        return ((ItemFusionWheel)ItemStack.of(stack.getTag().getCompound("FusionWheel")).getItem()).getAttack();
    }

    @Override
    public float getDefense(ItemStack stack) {
        return ((ItemFusionWheel)ItemStack.of(stack.getTag().getCompound("FusionWheel")).getItem()).getDefense();
    }
}
