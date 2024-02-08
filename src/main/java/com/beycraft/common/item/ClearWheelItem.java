package com.beycraft.common.item;

import com.beycraft.BeyTypes;
import com.beycraft.common.ability.AbilityType;
import com.beycraft.common.container.ClearWheelContainer;
import com.beycraft.common.container.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.concurrent.atomic.AtomicReference;

public class ClearWheelItem extends LayerItem {
    public ClearWheelItem(String name, String displayName, int rotationDirection, float weight, float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, rotationDirection, 0, 0, weight, burst, primaryAbility, secondaryAbility, type, resonanceColor, secondResonanceColor, thirdResonanceColor);

    }

    @Override
    protected StringTextComponent getLayerName(ItemStack stack) {
        AtomicReference<String> name = new AtomicReference<>(getDisplayName());

        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            if (!itemHandler.getStackInSlot(2).isEmpty())
                name.set(itemHandler.getStackInSlot(2).getHoverName().getString() + " " + name.get());
        });

        return new StringTextComponent(name.get());
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
