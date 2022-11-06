package com.beycraft.common.capability.item.beylogger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public interface IBeylogger {

    void setUrl(String url);

    String getUrl();

    void readNBT(CompoundNBT nbt);

    INBT writeNBT();
}
