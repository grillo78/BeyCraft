package com.beycraft.network.message;

import com.beycraft.common.entity.BeybladeEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class MessageActivateResonance implements IMessage<MessageActivateResonance> {

    @Override
    public void encode(MessageActivateResonance message, PacketBuffer buffer) {
    }

    @Override
    public MessageActivateResonance decode(PacketBuffer buffer) {
        return new MessageActivateResonance();
    }

    @Override
    public void handle(MessageActivateResonance message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = supplier.get().getSender();
            List<BeybladeEntity> beys = player.level.getLoadedEntitiesOfClass(BeybladeEntity.class, player.getBoundingBox().inflate(20));
            for (int i = 0; i < beys.size(); i++) {
                BeybladeEntity bey = beys.get(i);
                if (bey.getOwner().equals(player.getUUID()) && !bey.isOnResonance() && bey.getResonanceCooldown()==0)
                    bey.activateResonance();
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
