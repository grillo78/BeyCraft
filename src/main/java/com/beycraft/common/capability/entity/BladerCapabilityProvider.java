package com.beycraft.common.capability.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BladerCapabilityProvider implements ICapabilitySerializable {

    @CapabilityInject(IBlader.class)
    public static final Capability<IBlader> BLADER_CAP = null;
    private IBlader bladerValue = BLADER_CAP.getDefaultInstance();

    private final LazyOptional<IBlader> blader;

    public BladerCapabilityProvider(PlayerEntity player) {
        this.blader = LazyOptional.of(() ->bladerValue);
        bladerValue.setPlayer(player);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == BLADER_CAP) {
            return blader.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        INBT nbt = blader.map(blader -> BLADER_CAP.writeNBT(blader, null))
                .orElseGet(CompoundNBT::new);
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        blader.ifPresent(blader -> BLADER_CAP.readNBT(blader, null, nbt));
    }
}
