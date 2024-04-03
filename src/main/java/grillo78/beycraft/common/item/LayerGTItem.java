package grillo78.beycraft.common.item;

import grillo78.beycraft.BeyTypes;
import grillo78.beycraft.common.ability.AbilityType;
import grillo78.beycraft.common.container.LayerContainer;
import grillo78.beycraft.common.container.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public class LayerGTItem extends LayerItem{
    public LayerGTItem(String name, String displayName, int rotationDirection, float attack, float defense, float weight, float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, rotationDirection, attack, defense, weight, burst, primaryAbility, secondaryAbility, type, resonanceColor, secondResonanceColor, thirdResonanceColor);
    }

    @Override
    public int getSlotsAmount() {
        return 4;
    }

    @Override
    protected Container getLayerContainer(int id, ItemStack stack, PlayerInventory playerInventory) {
        return new LayerContainer(ModContainers.LAYER, id,
                stack, playerInventory);
    }
}
