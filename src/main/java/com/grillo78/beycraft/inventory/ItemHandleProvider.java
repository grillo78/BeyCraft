package com.grillo78.beycraft.inventory;

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

public class ItemHandleProvider implements ICapabilityProvider, ICapabilitySerializable {

	private final LazyOptional<IItemHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(3));

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public INBT serializeNBT() {
		return inventory.map(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(items, null))
				.orElseGet(CompoundNBT::new);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		inventory.ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, nbt));
	}

}
