package com.grillo78.beycraft.inventory;

import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.items.ItemBeyLayerGT;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemBeyProvider implements ICapabilityProvider, ICapabilitySerializable {

    private final LazyOptional<IItemHandler> inventory;

    public ItemBeyProvider(ItemStack stack) {
        if (stack.getItem() instanceof ItemBeyLayerGT) {
            inventory = LazyOptional.of(() -> new ItemStackHandler(4) {
                @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                    return !(stack.getItem() instanceof ItemBeyLayer);
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                    if (!isItemValid(slot, stack)) return stack;
                    return super.insertItem(slot, stack, simulate);
                }
            });
        } else {
            inventory = LazyOptional.of(() -> new ItemStackHandler(2) {
                @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                    return !(stack.getItem() instanceof ItemBeyLayer);
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                    if (!isItemValid(slot, stack)) return stack;
                    return super.insertItem(slot, stack, simulate);
                }
            });
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        INBT nbt = inventory.map(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(items, null))
                .orElseGet(CompoundNBT::new);
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        inventory.ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, nbt));
    }

}
