package com.beycraft.network.message;

import com.beycraft.utils.ClientUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageLoseCombat implements IMessage<MessageLoseCombat>{

@Override
public void encode(MessageLoseCombat message, PacketBuffer buffer) {

        }

@Override
public MessageLoseCombat decode(PacketBuffer buffer) {
        return new MessageLoseCombat();
        }

@Override
public void handle(MessageLoseCombat message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
        ClientUtils.RankingUtils.loseCombat();
        });
        supplier.get().setPacketHandled(true);
        }
}
