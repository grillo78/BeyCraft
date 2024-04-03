package grillo78.beycraft.common.container;

import grillo78.beycraft.common.capability.item.Launcher;
import grillo78.beycraft.common.capability.item.LauncherCapabilityProvider;
import grillo78.beycraft.common.container.slot.*;
import grillo78.beycraft.utils.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class LauncherContainer extends Container {

    public LauncherContainer(@Nullable ContainerType<?> type, int id, ItemStack stack, PlayerInventory playerInventory, boolean mainHand, Direction direction) {
        super(type, id);
        addSlots(stack.getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).orElseGet(() -> new Launcher()).getInventory(), direction);
        addPlayerSlots(new InvWrapper(playerInventory), playerInventory.selected, mainHand);
    }

    public LauncherContainer(@Nullable ContainerType<?> type, int id, ItemStack stack, boolean mainHand, PlayerInventory playerInventory) {
        super(type, id);
        addSlots(stack.getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).orElseGet(() -> new Launcher()).getInventory());
        addPlayerSlots(new InvWrapper(playerInventory), playerInventory.selected, mainHand);
    }

    protected void addSlots(IItemHandler cap, Direction direction) {
        this.addSlot(new BeySlot(cap, 0, 10, 15, direction));
        this.addOtherSlots(cap);
    }

    private void addOtherSlots(IItemHandler cap) {
        this.addSlot(new HandleSlot(cap, 1, 38, 15));
        this.addSlot(new BeyloggerSlot(cap, 2, 10, 35));
    }

    protected void addSlots(IItemHandler cap) {
        this.addSlot(new BeySlotDual(cap, 0, 10, 15));
        this.addOtherSlots(cap);
    }

    protected void addPlayerSlots(InvWrapper playerInventory, int locked, boolean mainHand) {
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
            if (row == locked && mainHand)
                this.addSlot(new LockedSlot(playerInventory, row, x, y));
            else
                this.addSlot(new SlotItemHandler(playerInventory, row, x, y));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
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
