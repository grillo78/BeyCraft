package com.beycraft.network.message;

import com.beycraft.utils.ClientUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageWinCombat implements IMessage<MessageWinCombat>{

    @Override
    public void encode(MessageWinCombat message, PacketBuffer buffer) {

    }

    @Override
    public MessageWinCombat decode(PacketBuffer buffer) {
        return new MessageWinCombat();
    }

    @Override
    public void handle(MessageWinCombat message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            ClientUtils.RankingUtils.winCombat();
        });
        supplier.get().setPacketHandled(true);
    }
}
