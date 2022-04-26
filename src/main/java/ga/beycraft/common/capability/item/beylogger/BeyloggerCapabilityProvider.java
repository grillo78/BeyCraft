package ga.beycraft.common.capability.item.beylogger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BeyloggerCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IBeylogger.class)
    public static final Capability<IBeylogger> BEYLOGGER = null;
    private IBeylogger beyloggerInstance;
    private LazyOptional<IBeylogger> beylogger;

    public BeyloggerCapabilityProvider() {
        if (BEYLOGGER != null){
            this.beyloggerInstance = BEYLOGGER.getDefaultInstance();
            this.beylogger = LazyOptional.of(()-> beyloggerInstance);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == BEYLOGGER){
            return beylogger.cast();
        }
        return LazyOptional.empty();
    }


    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) BEYLOGGER.getStorage().writeNBT(BEYLOGGER, beylogger.orElseThrow(NullPointerException::new), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        BEYLOGGER.getStorage().readNBT(BEYLOGGER, beylogger.orElseThrow(NullPointerException::new), null, nbt);
    }
}
