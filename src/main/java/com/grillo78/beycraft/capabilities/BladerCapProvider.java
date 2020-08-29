package com.grillo78.beycraft.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BladerCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(IBladerLevel.class)
    public static final Capability<IBladerLevel> BLADERLEVEL_CAP = null;
    private IBladerLevel bladerLevelInstance = BLADERLEVEL_CAP.getDefaultInstance();
    @CapabilityInject(ICurrency.class)
    public static final Capability<ICurrency> BLADERCURRENCY_CAP = null;
    private ICurrency currencyInstance = BLADERCURRENCY_CAP.getDefaultInstance();

    private LazyOptional<IBladerLevel> bladerLevel = LazyOptional.of(()-> bladerLevelInstance);

    private LazyOptional<ICurrency> currency = LazyOptional.of(()-> currencyInstance);


    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("bladerLevel",BLADERLEVEL_CAP.getStorage().writeNBT(BLADERLEVEL_CAP, bladerLevelInstance, null));
        nbt.put("currency",BLADERCURRENCY_CAP.getStorage().writeNBT(BLADERCURRENCY_CAP, currencyInstance, null));
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        BLADERLEVEL_CAP.getStorage().readNBT(BLADERLEVEL_CAP, bladerLevelInstance, null, ((CompoundNBT)nbt).get("bladerLevel"));
        BLADERCURRENCY_CAP.getStorage().readNBT(BLADERCURRENCY_CAP,currencyInstance, null, ((CompoundNBT)nbt).get("currency"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == BLADERLEVEL_CAP){
            return bladerLevel.cast();
        }
        if (BLADERCURRENCY_CAP == cap) {
            return currency.cast();
        }
        return LazyOptional.empty();
    }
}
