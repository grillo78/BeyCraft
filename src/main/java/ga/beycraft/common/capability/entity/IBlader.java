package ga.beycraft.common.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public interface IBlader {
    void readNBT(CompoundNBT nbt);

    INBT writeNBT();
}
