package ga.beycraft.common.capability.entity;

import ga.beycraft.common.launch.LaunchType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public interface IBlader {
    void readNBT(CompoundNBT nbt);

    INBT writeNBT();

    void syncToAll();

    void setLaunchType(LaunchType launchType);

    LaunchType getLaunchType();

    void setPlayer(PlayerEntity player);
}
