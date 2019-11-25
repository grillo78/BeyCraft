package com.grillo78.BeyCraft.inventory;

import com.grillo78.BeyCraft.tileentity.ExpositoryTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ExpositoryContainer extends Container {

	private ExpositoryTileEntity inventory;

	public ExpositoryContainer(InventoryPlayer inventory, ExpositoryTileEntity tileentity) {
		this.inventory = tileentity;
		this.addSlotToContainer(new SlotBeyLayerExpository(tileentity, 0, 56, 17));
		this.addSlotToContainer(new SlotBeyDiskExpository(tileentity, 1, 56, 27));
		this.addSlotToContainer(new SlotBeyDriverExpository(tileentity, 2, 56, 27));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
