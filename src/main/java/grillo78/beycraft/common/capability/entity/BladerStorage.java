package grillo78.beycraft.common.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class BladerStorage implements Capability.IStorage<Blader> {

    @Override
    public INBT writeNBT(Capability capability, Blader instance, Direction side) {
        return instance.writeNBT();
    }

    @Override
    public void readNBT(Capability capability, Blader instance, Direction side, INBT nbt) {
        instance.readNBT((CompoundNBT) nbt);
    }
}
