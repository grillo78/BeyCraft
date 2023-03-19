package com.beycraft.common.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class Wallet implements INBTSerializable<CompoundNBT> {
    private float currency = 0;

    public float getCurrency() {
        return currency;
    }

    public void increaseCurrency(float currency) {
        this.currency += currency;
    }

    public void setCurrency(float currency) {
        this.currency = currency;
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putFloat("currency", currency);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        currency = nbt.getFloat("currency");
    }
}
