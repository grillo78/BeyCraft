package com.beycraft.common.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class BladerStorage implements Capability.IStorage<IBlader> {

    @Override
    public INBT writeNBT(Capability capability, IBlader instance, Direction side) {
        return instance.writeNBT();
    }

    @Override
    public void readNBT(Capability capability, IBlader instance, Direction side, INBT nbt) {
        instance.readNBT((CompoundNBT) nbt);
    }
}
