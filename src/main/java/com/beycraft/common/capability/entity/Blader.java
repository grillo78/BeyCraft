package com.beycraft.common.capability.entity;

import com.beycraft.common.launch.LaunchType;
import com.beycraft.common.launch.LaunchTypes;
import com.beycraft.network.PacketHandler;
import com.beycraft.network.message.MessageSyncBladerCap;
import com.beycraft.network.message.MessageToServerSyncBladerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;

public class Blader implements IBlader {

    private LaunchType launchType = LaunchTypes.BASIC_LAUNCH_TYPE;
    private PlayerEntity player;
    private Level bladerLevel = new Level();

    @Override
    public void readNBT(CompoundNBT nbt) {

        if (nbt.contains("launchType") && LaunchType.LAUNCH_TYPES.getValue(new ResourceLocation(nbt.getString("launchType"))) != null)
            launchType = LaunchType.LAUNCH_TYPES.getValue(new ResourceLocation(nbt.getString("launchType")));
        if (nbt.contains("bladerLevel"))
            bladerLevel = Level.readFromNBT(nbt.getCompound("bladerLevel"));
    }

    @Override
    public INBT writeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();

        compoundNBT.putString("launchType", launchType.getRegistryName().toString());
        compoundNBT.put("bladerLevel", bladerLevel.writeNBT());
        return compoundNBT;
    }

    @Override
    public void syncToAll() {
        if (!player.level.isClientSide) {
            for (PlayerEntity playerAux : player.level.players()) {
                PacketHandler.INSTANCE.sendTo(new MessageSyncBladerCap((CompoundNBT) writeNBT(), player.getId()), ((ServerPlayerEntity) playerAux).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            }
        } else {
            PacketHandler.INSTANCE.sendToServer(new MessageToServerSyncBladerCap((CompoundNBT) writeNBT()));
        }
    }

    @Override
    public LaunchType getLaunchType() {
        return launchType;
    }

    @Override
    public void setLaunchType(LaunchType launchType) {
        if (launchType != null)
            this.launchType = launchType;
    }

    @Override
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public Level getBladerLevel() {
        return this.bladerLevel;
    }
}
