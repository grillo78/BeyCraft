package com.grillo78.beycraft.capabilities;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CurrencyStorage implements IStorage<ICurrency> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<ICurrency> capability, ICurrency instance, Direction side) {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putFloat("Currency", instance.getCurrency());
        return compoundNBT;
    }

    @Override
    public void readNBT(Capability<ICurrency> capability, ICurrency instance, Direction side, INBT nbt) {
        instance.setCurrency(((CompoundNBT) nbt).getFloat("Currency"));
    }
}