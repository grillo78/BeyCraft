package com.grillo78.BeyCraft.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class BladerLevelStorage implements IStorage<IBladerLevel> {

	@Override
	public NBTBase writeNBT(Capability<IBladerLevel> capability, IBladerLevel instance, EnumFacing side) {
		return new NBTTagFloat(instance.getBladerLevel());
	}

	@Override
	public void readNBT(Capability<IBladerLevel> capability, IBladerLevel instance, EnumFacing side, NBTBase nbt) {
		instance.setBladerLevel(((NBTPrimitive) nbt).getFloat());
	}

}
