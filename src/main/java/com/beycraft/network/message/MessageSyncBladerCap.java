package com.beycraft.network.message;

import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncBladerCap implements IMessage<MessageSyncBladerCap>{

    private CompoundNBT nbt;
    private int playerID;

    public MessageSyncBladerCap() {
    }

    public MessageSyncBladerCap(CompoundNBT nbt, int playerID) {
        this.nbt = nbt;
        this.playerID = playerID;
    }

    @Override
    public void encode(MessageSyncBladerCap message, PacketBuffer buffer) {
        buffer.writeNbt(message.nbt);
        buffer.writeInt(message.playerID);
    }

    @Override
    public MessageSyncBladerCap decode(PacketBuffer buffer) {
        return new MessageSyncBladerCap(buffer.readAnySizeNbt(), buffer.readInt());
    }

    @Override
    public void handle(MessageSyncBladerCap message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            Entity player = Minecraft.getInstance().level.getEntity(message.playerID);
            player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(cap->{
                cap.readNBT(message.nbt);
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
