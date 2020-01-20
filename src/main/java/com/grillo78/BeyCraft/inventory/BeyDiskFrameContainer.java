package com.grillo78.BeyCraft.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;



public class BeyDiskFrameContainer extends Container {


	/**
	 * @param type
	 * @param id
	 */
	public BeyDiskFrameContainer(ContainerType<?> type, int id, ItemStack disk, PlayerInventory playerInventory,
			PlayerEntity player) {
		super(type, id);
		if(disk!=null) {
			disk.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			.ifPresent(h -> this.addSlot(new SlotBeyFrame(h, 0, 10, 10)));
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack transferred = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        int otherSlots = this.inventorySlots.size() - 36;

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            transferred = current.copy();

            if (index < otherSlots) {
                if (!this.mergeItemStack(current, otherSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(current, 0, otherSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (current.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return transferred;
	}
	
}
