package ga.beycraft.common.capability.item.beylogger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class BeyloggerStorage implements Capability.IStorage<IBeylogger> {

    @Override
    public INBT writeNBT(Capability capability, IBeylogger instance, Direction side) {
        return instance.writeNBT();
    }

    @Override
    public void readNBT(Capability capability, IBeylogger instance, Direction side, INBT nbt) {
        instance.readNBT((CompoundNBT) nbt);
    }
}
