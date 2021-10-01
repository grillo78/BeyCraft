package ga.beycraft.items;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.util.BeyTypes;
import net.minecraft.item.ItemStack;

public class ItemBeyLayerGod extends ItemBeyLayer {
    public ItemBeyLayerGod(String name, float rotationDirection, float attack, float defense, float weight, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, rotationDirection, attack, defense, weight, 0, primaryAbility, secundaryAbility, type);
        BeyCraftRegistry.ITEMSLAYERGT.add(this);
    }

    @Override
    public float getWeight(ItemStack stack) {
        return super.getWeight(stack)+((ItemBeyGodChip)ItemStack.of(stack.getTag().getCompound("chip")).getItem()).getWeight();
    }
}
