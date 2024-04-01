package com.beycraft.common.capability.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public interface ILauncher extends INBTSerializable<CompoundNBT> {
    IItemHandler getInventory();

    int getRed();

    int getGreen();

    int getBlue();

    void setColor(int red, int green, int blue);
}
