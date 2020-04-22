package com.grillo78.beycraft.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BladerLevelProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(IBladerLevel.class)
    public static final Capability<IBladerLevel> BLADERLEVEL_CAP = null;
    private LazyOptional<IBladerLevel> bladerLevel = LazyOptional.of(()-> new BladerLevel());

    private IBladerLevel instance = BLADERLEVEL_CAP.getDefaultInstance();


    @Override
    public INBT serializeNBT() {
        return BLADERLEVEL_CAP.getStorage().writeNBT(BLADERLEVEL_CAP, instance, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        BLADERLEVEL_CAP.getStorage().readNBT(BLADERLEVEL_CAP, instance, null, nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == BLADERLEVEL_CAP){
            return bladerLevel.cast();
        }
        return LazyOptional.empty();
    }
}
