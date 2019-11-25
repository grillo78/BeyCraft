package com.grillo78.BeyCraft.inventory;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("rawtypes")
public class BeyBladeProvider implements ICapabilityProvider, ICapabilitySerializable{

	private final ItemStackHandler inventory;
	
	public BeyBladeProvider() {
		inventory = new ItemStackHandler(5);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability( Capability<T> capability, EnumFacing facing ) {
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
			return (T) inventory; 
		}
		return null;
	}

	@Override
	public NBTBase serializeNBT() {
		return inventory.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		inventory.deserializeNBT((NBTTagCompound) nbt);
		
	}

}
