package com.grillo78.beycraft.inventory;

import com.lazy.baubles.api.BaubleType;
import com.lazy.baubles.api.IBauble;
import com.lazy.baubles.api.cap.BaublesCapabilities;
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

public class ItemBeltProvider implements ICapabilityProvider, ICapabilitySerializable {

	private final LazyOptional<IItemHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(2));
	private final LazyOptional<IBauble> iBaubleCap = LazyOptional.of(() -> new IBauble() {
		@Override
		public BaubleType getBaubleType() {
			return BaubleType.BELT;
		}
	});

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		if (cap == BaublesCapabilities.ITEM_BAUBLE) {
			return BaublesCapabilities.ITEM_BAUBLE.orEmpty(cap, iBaubleCap);
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
