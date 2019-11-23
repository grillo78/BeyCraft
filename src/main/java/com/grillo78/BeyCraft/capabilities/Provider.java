package com.grillo78.BeyCraft.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class Provider implements ICapabilitySerializable<NBTBase>{

	@CapabilityInject(IBladerLevel.class)
	public static final Capability<IBladerLevel> BLADERLEVEL_CAP = null;
	
	private IBladerLevel instance = BLADERLEVEL_CAP.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability==BLADERLEVEL_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == BLADERLEVEL_CAP ? BLADERLEVEL_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return BLADERLEVEL_CAP.getStorage().writeNBT(BLADERLEVEL_CAP, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		BLADERLEVEL_CAP.getStorage().readNBT(BLADERLEVEL_CAP, instance, null, nbt);
		
	}

}
