package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.capabilities.BladerLevelProvider;
import com.grillo78.beycraft.tileentity.BeyCreatorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncBladerLevel implements IMessage<MessageSyncBladerLevel> {

    private int bladerLevel;
    private float experience;

    public MessageSyncBladerLevel(){}

    public MessageSyncBladerLevel(int bladerLevel,float expreience){
        this.bladerLevel = bladerLevel;
        this.experience = expreience;
    }

    @Override
    public void encode(MessageSyncBladerLevel message, PacketBuffer buffer) {
        buffer.writeInt(message.bladerLevel);
        buffer.writeFloat(message.experience);
    }

    @Override
    public MessageSyncBladerLevel decode(PacketBuffer buffer) {
        return new MessageSyncBladerLevel(buffer.readInt(),buffer.readFloat());
    }

    @Override
    public void handle(MessageSyncBladerLevel message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            Minecraft.getInstance().player.getCapability(BladerLevelProvider.BLADERLEVEL_CAP).ifPresent(h->{
                h.setBladerLevel(message.bladerLevel);
                h.setExperience(message.experience);
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
