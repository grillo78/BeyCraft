package com.grillo78.beycraft.inventory.slots;

import com.grillo78.beycraft.items.*;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyLayer extends SlotItemHandler {

    private ItemStack launcher;

    public SlotBeyLayer(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemStack launcher) {
        super(itemHandler, index, xPosition, yPosition);
        this.launcher = launcher;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        boolean[] isValid = {false};
        if(!(launcher.getItem() instanceof ItemDualLauncher)){
            if (stack.getItem() instanceof ItemBeyLayer && (((ItemBeyLayer) stack.getItem()).getRotationDirection() == ((ItemLauncher)launcher.getItem()).getRotation(launcher) || stack.getItem() instanceof ItemBeyLayerDual || stack.getItem() instanceof ItemBeyLayerGTDual)) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    if (stack.hasTag()) {
                        switch (h.getSlots()) {
                            case 2:
                                if (ItemStack.read((CompoundNBT) stack.getTag().get("disc")).getItem() instanceof ItemBeyDisc && ItemStack.read((CompoundNBT) stack.getTag().get("driver")).getItem() instanceof ItemBeyDriver) {
                                    isValid[0] = true;
                                }
                                break;
                            case 4:
                                if (ItemStack.read((CompoundNBT) stack.getTag().get("disc")).getItem() instanceof ItemBeyDisc
                                        && ItemStack.read((CompoundNBT) stack.getTag().get("driver")).getItem() instanceof ItemBeyDriver
                                        && ItemStack.read((CompoundNBT) stack.getTag().get("chip")).getItem() instanceof ItemBeyGTChip
                                        && ItemStack.read((CompoundNBT) stack.getTag().get("weight")).getItem() instanceof ItemBeyGTWeight) {
                                    isValid[0] = true;
                                }
                                break;
                        }
                    }
                });
            }
        } else {
            if (stack.getItem() instanceof ItemBeyLayerDual || stack.getItem() instanceof ItemBeyLayerGTDual) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    if (stack.hasTag()) {
                        switch (h.getSlots()) {
                            case 2:
                                if (ItemStack.read((CompoundNBT) stack.getTag().get("disc")).getItem() instanceof ItemBeyDisc && ItemStack.read((CompoundNBT) stack.getTag().get("driver")).getItem() instanceof ItemBeyDriver) {
                                    isValid[0] = true;
                                }
                                break;
                            case 4:
                                if (ItemStack.read((CompoundNBT) stack.getTag().get("disc")).getItem() instanceof ItemBeyDisc
                                        && ItemStack.read((CompoundNBT) stack.getTag().get("driver")).getItem() instanceof ItemBeyDriver
                                        && ItemStack.read((CompoundNBT) stack.getTag().get("chip")).getItem() instanceof ItemBeyGTChip
                                        && ItemStack.read((CompoundNBT) stack.getTag().get("weight")).getItem() instanceof ItemBeyGTWeight) {
                                    isValid[0] = true;
                                }
                                break;
                        }
                    }
                });
            }
        }
        return isValid[0];
    }

}
