package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.ItemBeyDisc;
import com.grillo78.beycraft.items.ItemBeyGTChip;
import com.grillo78.beycraft.items.ItemBeyGTChipWeight;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotBeyGTChip extends SlotItemHandler {

    public SlotBeyGTChip(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        if(getItemHandler().getStackInSlot(2).getItem() instanceof ItemBeyGTChip){
            ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(3, ItemStack.EMPTY);
        }
        return super.onTake(thePlayer, stack);
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        if (stack.getItem() instanceof ItemBeyGTChip) {
            ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(3, stack);
        }
        super.putStack(stack);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemBeyGTChip;
    }
}
