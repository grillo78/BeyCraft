package ga.beycraft.common.capability.entity;

import ga.beycraft.common.launch.LaunchType;
import ga.beycraft.common.launch.LaunchTypes;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageSyncBladerCap;
import ga.beycraft.network.message.MessageToServerSyncBladerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;

public class Blader implements IBlader{

    private LaunchType launchType = LaunchTypes.BASIC_LAUNCH_TYPE;
    private PlayerEntity player;

    @Override
    public void readNBT(CompoundNBT nbt) {

        if(nbt.contains("launchType"))
            launchType = LaunchType.LAUNCH_TYPES.getValue(new ResourceLocation(nbt.getString("launchType")));

    }

    @Override
    public INBT writeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();

        compoundNBT.putString("launchType", launchType.getRegistryName().toString());

        return compoundNBT;
    }

    @Override
    public void syncToAll() {
        if(!player.level.isClientSide){
            for (PlayerEntity playerAux : player.level.players()) {
                PacketHandler.INSTANCE.sendTo(new MessageSyncBladerCap((CompoundNBT) writeNBT(), player.getId()),((ServerPlayerEntity)playerAux).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
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
        this.launchType = launchType;
    }

    @Override
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
