package com.grillo78.beycraft.inventory.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BeyLoggerContainer extends Container {

    /**
     * @param type
     * @param id
     */
    public BeyLoggerContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

}
