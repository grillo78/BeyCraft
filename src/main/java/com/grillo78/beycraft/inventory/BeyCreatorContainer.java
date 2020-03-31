package com.grillo78.beycraft.inventory;

import com.grillo78.beycraft.tileentity.BeyCreatorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class BeyCreatorContainer extends Container {

    public BeyCreatorContainer(@Nullable ContainerType<?> containerType, int id, BeyCreatorTileEntity tileEntity) {
        super(containerType, id);
        tileEntity.getInventory().ifPresent(h -> {
//            this.addSlot(new SlotItemHandler(h, 0, Integer.MIN_VALUE, Integer.MIN_VALUE));
//            this.addSlot(new SlotItemHandler(h, 1, -15, -15));
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
