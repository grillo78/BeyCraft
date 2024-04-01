package com.beycraft.common.container;

import com.beycraft.common.container.slot.GTWeightSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class GTLayerContainer extends LayerContainer {
    public GTLayerContainer(ContainerType<GTLayerContainer> gtLayer, int windowId, ItemStack empty, PlayerInventory inv) {
        super(gtLayer, windowId, empty, inv);
    }

    @Override
    protected int getSlotsAmount() {
        return 4;
    }

    @Override
    protected void addSlots(IItemHandler cap) {
        super.addSlots(cap);
        this.addSlot(new GTWeightSlot(cap,2,38,15));
    }
}
