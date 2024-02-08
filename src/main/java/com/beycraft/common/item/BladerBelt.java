package com.beycraft.common.item;

import com.beycraft.common.capability.item.BeltCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.heroesunited.heroesunited.common.objects.container.EquipmentAccessoriesSlot;

import javax.annotation.Nullable;

public class BladerBelt extends AccessoryItem{

    public BladerBelt() {
        super(EquipmentAccessoriesSlot.BELT);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BeltCapabilityProvider();
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();

        CompoundNBT combinedTag = new CompoundNBT();

        if (baseTag != null) {
            combinedTag.put("base_tag", baseTag);
        }

        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            INBT capabilityTag = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(cap, null);

            if (capabilityTag != null) {
                combinedTag.put("capability_tag", capabilityTag);
            }
        });

        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundNBT baseTag = nbt.getCompound("base_tag"); // empty if not found
        INBT capabilityTag = nbt.get("capability_tag");    // empty if not found
        stack.setTag(baseTag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, capabilityTag));
    }
}
