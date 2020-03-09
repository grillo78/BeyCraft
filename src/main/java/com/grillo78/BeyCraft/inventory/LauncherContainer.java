package com.grillo78.BeyCraft.inventory;

import com.grillo78.BeyCraft.inventory.slots.SlotBeyLayer;
import com.grillo78.BeyCraft.inventory.slots.SlotBeyLogger;
import com.grillo78.BeyCraft.inventory.slots.SlotHandle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;



public class LauncherContainer extends Container {


	/**
	 * @param type
	 * @param id
	 */
	public LauncherContainer(ContainerType<?> type, int id, ItemStack launcher, PlayerInventory playerInventory,
			PlayerEntity player, int rotation) {
		super(type, id);
		launcher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				.ifPresent(h -> this.addSlot(new SlotBeyLayer(h, 0, 10, 10, rotation)));
		launcher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				.ifPresent(h -> this.addSlot(new SlotHandle(h, 1, 62, 10)));
		launcher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				.ifPresent(h -> this.addSlot(new SlotBeyLogger(h, 2, 62, 30)));
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
