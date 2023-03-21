package com.beycraft.common.capability.entity;

import com.beycraft.common.launch.LaunchType;
import com.beycraft.common.launch.LaunchTypes;
import com.beycraft.common.ranking.Level;
import com.beycraft.network.PacketHandler;
import com.beycraft.network.message.MessageSyncBladerCap;
import com.beycraft.network.message.MessageToServerSyncBladerCap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;

public class Blader {

    private LaunchType launchType = LaunchTypes.BASIC_LAUNCH_TYPE;
    private PlayerEntity player;
    private Level bladerLevel = new Level();
    private Wallet wallet = new Wallet();
    private int animatorID = 0;
    private boolean launching = false;

    public void readNBT(CompoundNBT nbt) {

        if (nbt.contains("launchType") && LaunchType.LAUNCH_TYPES.getValue(new ResourceLocation(nbt.getString("launchType"))) != null)
            launchType = LaunchType.LAUNCH_TYPES.getValue(new ResourceLocation(nbt.getString("launchType")));
        if (nbt.contains("bladerLevel"))
            bladerLevel = Level.readFromNBT(nbt.getCompound("bladerLevel"));
        if (nbt.contains("wallet"))
            wallet.deserializeNBT(nbt.getCompound("wallet"));
        if (nbt.contains("animatorID"))
            animatorID = nbt.getInt("animatorID");
    }

    public CompoundNBT writeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();

        compoundNBT.putString("launchType", launchType.getRegistryName().toString());
        compoundNBT.put("bladerLevel", bladerLevel.writeNBT());
        compoundNBT.put("wallet", wallet.serializeNBT());
        compoundNBT.putInt("animatorID", animatorID);
        return compoundNBT;
    }

    private CompoundNBT writeNetwork(){
        CompoundNBT compoundNBT = writeNBT();

        compoundNBT.putBoolean("launching", launching);

        return compoundNBT;
    }

    public void readNetwork(CompoundNBT nbt){
        readNBT(nbt);
        launching = nbt.getBoolean("launching");
    }

    public boolean isLaunching() {
        return launching;
    }

    public void setLaunching(boolean launching) {
        this.launching = launching;
    }

    public void syncToAll() {
        if (!player.level.isClientSide) {
            for (PlayerEntity playerAux : player.level.players()) {
                PacketHandler.INSTANCE.sendTo(new MessageSyncBladerCap((CompoundNBT) writeNetwork(), player.getId()), ((ServerPlayerEntity) playerAux).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            }
        } else {
            if(player == Minecraft.getInstance().player)PacketHandler.INSTANCE.sendToServer(new MessageToServerSyncBladerCap((CompoundNBT) writeNetwork()));
        }
    }

    public LaunchType getLaunchType() {
        return launchType;
    }

    public void setLaunchType(LaunchType launchType) {
        if (launchType != null)
            this.launchType = launchType;
    }

    public void setAnimatorID(int animatorID) {
        this.animatorID = animatorID;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public Level getBladerLevel() {
        return this.bladerLevel;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public int getAnimatorID() {
        return animatorID;
    }
}
