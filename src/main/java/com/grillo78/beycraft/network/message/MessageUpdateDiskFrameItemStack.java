package com.grillo78.beycraft.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.function.Supplier;

public class MessageUpdateDiskFrameItemStack implements IMessage<MessageUpdateDiskFrameItemStack> {

    private ItemStack stack;
    private ItemStack frame;
    private Hand hand;

    public MessageUpdateDiskFrameItemStack(){}

    public MessageUpdateDiskFrameItemStack(ItemStack stack, ItemStack frame, Hand hand){
        this.stack = new ItemStack(stack.getItem());
        this.frame = frame;
        this.hand = hand;
    }

    @Override
    public void encode(MessageUpdateDiskFrameItemStack message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
        buffer.writeItemStack(message.frame);
        buffer.writeEnumValue(message.hand);
    }

    @Override
    public MessageUpdateDiskFrameItemStack decode(PacketBuffer buffer) {
        return new MessageUpdateDiskFrameItemStack(buffer.readItemStack(),buffer.readItemStack(),buffer.readEnumValue(Hand.class));
    }

    @Override
    public void handle(MessageUpdateDiskFrameItemStack message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() ->{
            message.stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
                h.insertItem(0,message.frame,false);
            });
            supplier.get().getSender().setHeldItem(message.hand,message.stack);
        });
        supplier.get().setPacketHandled(true);
    }
}
