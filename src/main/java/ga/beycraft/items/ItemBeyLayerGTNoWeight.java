package ga.beycraft.items;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.util.BeyTypes;
import net.minecraft.item.ItemStack;

public class ItemBeyLayerGTNoWeight extends ItemBeyLayer {
    public ItemBeyLayerGTNoWeight(String name, String displayName, float rotationDirection, float attack, float defense, float weight, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, displayName, rotationDirection, attack, defense, weight, 0, primaryAbility, secundaryAbility, type, null, null, null);
        BeyCraftRegistry.ITEMSLAYERGTNOWEIGHT.add(this);
    }

    @Override
    public float getWeight(ItemStack stack) {
        return super.getWeight(stack)+((ItemBeyGTChip)ItemStack.of(stack.getTag().getCompound("chip")).getItem()).getWeight();
    }

    @Override
    public float getBurst(ItemStack stack) {
        return ((ItemBeyGTChip)ItemStack.of(stack.getTag().getCompound("chip")).getItem()).getBurst();
    }
}
