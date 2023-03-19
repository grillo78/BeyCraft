package com.beycraft.common.item;

import com.beycraft.BeyTypes;
import com.beycraft.ability.AbilityType;
import com.beycraft.common.container.ClearWheelContainer;
import com.beycraft.common.container.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public class ClearWheelItem extends LayerItem {
    public ClearWheelItem(String name, String displayName, int rotationDirection, float weight, float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, rotationDirection, 0, 0, weight, burst, primaryAbility, secondaryAbility, type, resonanceColor, secondResonanceColor, thirdResonanceColor);

    }

    @Override
    public int getSlotsAmount() {
        return 3;
    }

    @Override
    protected Container getLayerContainer(int id, ItemStack stack, PlayerInventory playerInventory) {
        return new ClearWheelContainer(ModContainers.CLEAR_WHEEL, id, stack, playerInventory);
    }
}
