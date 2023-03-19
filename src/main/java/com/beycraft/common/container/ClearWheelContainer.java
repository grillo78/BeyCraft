package com.beycraft.common.container;

import com.beycraft.common.container.slot.FusionWheelSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class ClearWheelContainer extends LayerContainer {
    public ClearWheelContainer(@Nullable ContainerType<?> type, int id, ItemStack stack, PlayerInventory playerInventory) {
        super(type, id, stack, playerInventory);
    }

    @Override
    protected int getSlotsAmount() {
        return 3;
    }

    @Override
    protected void addSlots(IItemHandler cap) {
        super.addSlots(cap);
        this.addSlot(new FusionWheelSlot(cap, 2, 38, 15));
    }
}
