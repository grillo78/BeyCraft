package com.grillo78.beycraft.inventory;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.inventory.slots.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BeltContainer extends Container {

	private boolean canBeOpened = true;
	private ItemStack stack;

	/**
	 * @param type
	 * @param id
	 */
	public BeltContainer(ContainerType<?> type, int id, ItemStack stack, PlayerInventory playerInventory,
			boolean locked) {
		super(type, id);
		this.stack = stack;
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			this.addSlot(new SlotBeyBothLayer(h, 0, 10, 10));
			this.addSlot(new SlotBeyLauncher(h, 1, 10, 30));
		});
		if (locked) {
			addPlayerSlots(new InvWrapper(playerInventory), playerInventory.currentItem);
		} else {
			addPlayerSlots(new InvWrapper(playerInventory), playerInventory.getSizeInventory());
		}
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		playerIn.playSound(BeyRegistry.OPEN_CLOSE_BELT, SoundCategory.PLAYERS, 1, 1);
		if (playerIn instanceof ServerPlayerEntity) {
			stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
				if(!stack.hasTag()){
					CompoundNBT nbt = new CompoundNBT();
					stack.setTag(nbt);
				}
				CompoundNBT nbt = stack.getTag();
				nbt.putBoolean("isEntity", false);
				nbt.put("bey", h.getStackInSlot(0).write(new CompoundNBT()));
				nbt.put("launcher", h.getStackInSlot(1).write(new CompoundNBT()));
			});
		}
	}

	protected void addPlayerSlots(InvWrapper playerinventory, int locked) {
		int yStart = 30 + 18 * 3;
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + yStart;
				this.addSlot(new SlotItemHandler(playerinventory, col + row * 9 + 9, x, y));
			}
		}

		for (int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			int y = yStart + 58;
			if (row != locked)
				this.addSlot(new SlotItemHandler(playerinventory, row, x, y));
			else
				this.addSlot(new LockedSlot(playerinventory, row, x, y));
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return canBeOpened;
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
