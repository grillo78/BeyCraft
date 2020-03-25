package com.grillo78.beycraft.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.function.Supplier;

public class MessageUpdateLayerItemStack implements IMessage<MessageUpdateLayerItemStack> {

    private ItemStack stack;
    private ItemStack disk;
    private ItemStack driver;
    private Hand hand;

    public MessageUpdateLayerItemStack(){}

    public MessageUpdateLayerItemStack(ItemStack stack, ItemStack disk, ItemStack driver, Hand hand){
        this.stack = new ItemStack(stack.getItem());
        this.disk = disk;
        this.driver = driver;
        this.hand = hand;
    }

    @Override
    public void encode(MessageUpdateLayerItemStack message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
        buffer.writeItemStack(message.disk);
        buffer.writeItemStack(message.driver);
        buffer.writeEnumValue(message.hand);
    }

    @Override
    public MessageUpdateLayerItemStack decode(PacketBuffer buffer) {
        return new MessageUpdateLayerItemStack(buffer.readItemStack(),buffer.readItemStack(),buffer.readItemStack(),buffer.readEnumValue(Hand.class));
    }

    @Override
    public void handle(MessageUpdateLayerItemStack message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() ->{
            message.stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
                h.insertItem(0,message.disk,false);
                h.insertItem(1,message.driver,false);
            });
            supplier.get().getSender().setHeldItem(message.hand,message.stack);
        });
        supplier.get().setPacketHandled(true);
    }
}
