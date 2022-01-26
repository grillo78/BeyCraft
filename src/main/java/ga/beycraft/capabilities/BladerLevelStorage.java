package ga.beycraft.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

import javax.annotation.Nullable;

public class BladerLevelStorage implements IStorage<IBladerLevel> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IBladerLevel> capability, IBladerLevel instance, Direction side) {
        CompoundNBT compoundNBT = new CompoundNBT();
        return compoundNBT;
    }

    @Override
    public void readNBT(Capability<IBladerLevel> capability, IBladerLevel instance, Direction side, INBT nbt) {
    }
}