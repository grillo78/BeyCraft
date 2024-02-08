package com.beycraft.common.container;

import com.beycraft.common.container.slot.BeltBeySlot;
import com.beycraft.common.container.slot.BeltLauncherSlot;
import com.beycraft.common.container.slot.FusionWheelSlot;
import com.beycraft.common.container.slot.LockedSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class BeltContainer extends Container {

    public BeltContainer(@Nullable ContainerType<?> pMenuType, int pContainerId, ItemStack stack, PlayerInventory playerInventory) {
        super(pMenuType, pContainerId);

        IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(() -> new ItemStackHandler(2));

        addSlot(new BeltBeySlot(cap, 0, 10, 15));
        addSlot(new BeltLauncherSlot(cap, 1, 10, 35));
        addPlayerSlots(new InvWrapper(playerInventory), playerInventory.selected);
    }

    protected void addPlayerSlots(InvWrapper playerInventory, int locked) {
        int yStart = 30 + 18 * 3;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + yStart;
                this.addSlot(new SlotItemHandler(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = yStart + 58;
            if (row != locked)
                this.addSlot(new SlotItemHandler(playerInventory, row, x, y));
            else
                this.addSlot(new LockedSlot(playerInventory, row, x, y));
        }
    }
    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack transferred = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        int otherSlots = this.slots.size() - 36;

        if (slot != null && slot.hasItem()) {
            ItemStack current = slot.getItem();
            transferred = current.copy();

            if (index < otherSlots) {
                if (!this.moveItemStackTo(current, otherSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(current, 0, otherSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (current.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return transferred;
    }
}
