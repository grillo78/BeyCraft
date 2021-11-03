package ga.beycraft.inventory;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.inventory.slots.LockedSlot;
import ga.beycraft.inventory.slots.SlotBeyDisk;
import ga.beycraft.inventory.slots.SlotBeyDriver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BeyContainer extends Container {

    private ItemStack stack;

    /**
     * @param type
     * @param id
     */
    public BeyContainer(ContainerType<?> type, int id, ItemStack stack, PlayerInventory playerInventory) {
        super(type, id);
        this.stack = stack;
        if(!BeyCraftRegistry.ITEMSLAYER.isEmpty()){
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {

                if(stack.hasTag()){
                    h.insertItem(0, ItemStack.of(stack.getTag().getCompound("disc")),false);
                    h.insertItem(1, ItemStack.of(stack.getTag().getCompound("driver")),false);
                }
                this.addSlot(new SlotBeyDisk(h, 0, 10, 15));
                this.addSlot(new SlotBeyDriver(h, 1, 10, 35));
            });
        }
        addPlayerSlots(new InvWrapper(playerInventory), playerInventory.selected);
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
    public void removed(PlayerEntity player) {
        super.removed(player);
        if (player instanceof ServerPlayerEntity) {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                if(!stack.hasTag()){
                    CompoundNBT nbt = new CompoundNBT();
                    stack.setTag(nbt);
                }
                CompoundNBT nbt = stack.getTag();
                nbt.putBoolean("isEntity", false);
                nbt.put("disc", h.getStackInSlot(0).save(new CompoundNBT()));
                nbt.put("driver", h.getStackInSlot(1).save(new CompoundNBT()));
            });
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
