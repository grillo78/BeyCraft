package com.grillo78.beycraft.network.message;

import java.util.function.Supplier;

import com.grillo78.beycraft.items.ItemBeyLogger;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageUpdateUrlBeyLogger implements IMessage<MessageUpdateUrlBeyLogger> {

    private String url;

    public MessageUpdateUrlBeyLogger() {
    }

    public MessageUpdateUrlBeyLogger(String url) {
        this.url = url;
    }

    @Override
    public void encode(MessageUpdateUrlBeyLogger message, PacketBuffer buffer) {
        buffer.writeInt(message.url.length());
        buffer.writeUtf(message.url);
    }

    @Override
    public MessageUpdateUrlBeyLogger decode(PacketBuffer buffer) {
        return new MessageUpdateUrlBeyLogger(buffer.readUtf(buffer.readInt()));
    }

    @Override
    public void handle(MessageUpdateUrlBeyLogger message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ItemStack beyLogger;
            if (supplier.get().getSender().getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemBeyLogger) {
                beyLogger = supplier.get().getSender().getItemInHand(Hand.MAIN_HAND);
            } else {
                beyLogger = supplier.get().getSender().getItemInHand(Hand.OFF_HAND);
            }
            if (!beyLogger.hasTag()) {
                CompoundNBT compound = new CompoundNBT();
                beyLogger.setTag(compound);
            }
            CompoundNBT compound = beyLogger.getTag();
            compound.putString("url",message.url);
            beyLogger.setTag(compound);

        });
        supplier.get().setPacketHandled(true);
    }
}
