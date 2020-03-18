package com.grillo78.beycraft.inventory.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class LockedSlot extends SlotItemHandler {
    public LockedSlot(InvWrapper playerinventory, int row, int x, int y) {
        super(playerinventory,row,x,y);
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return false;
    }
}