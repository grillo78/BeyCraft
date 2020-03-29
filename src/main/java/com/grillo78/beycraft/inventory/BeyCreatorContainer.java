package com.grillo78.beycraft.inventory;

import com.grillo78.beycraft.tileentity.BeyCreatorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

public class BeyCreatorContainer extends Container {

    private BeyCreatorTileEntity tileEntity;

    public BeyCreatorContainer(@Nullable ContainerType<?> containerType, int id, BeyCreatorTileEntity tileEntity) {
        super(containerType, id);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

}
