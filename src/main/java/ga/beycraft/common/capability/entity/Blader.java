package ga.beycraft.common.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class Blader implements IBlader{
    @Override
    public void readNBT(CompoundNBT nbt) {

    }

    @Override
    public INBT writeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        return compoundNBT;
    }
}
