package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.events.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageUpdateDiskFrameItemStack implements IMessage<MessageUpdateDiskFrameItemStack> {

    private ItemStack stack;
    private ItemStack frame;
    private Hand hand;
    private UUID uuid;

    public MessageUpdateDiskFrameItemStack() {
    }

    public MessageUpdateDiskFrameItemStack(ItemStack stack, ItemStack frame, Hand hand, UUID uuid) {
        this.stack = new ItemStack(stack.getItem());
        this.frame = frame;
        this.hand = hand;
        this.uuid = uuid;
    }

    @Override
    public void encode(MessageUpdateDiskFrameItemStack message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
        buffer.writeItemStack(message.frame);
        buffer.writeEnumValue(message.hand);
        buffer.writeUniqueId(message.uuid);
    }

    @Override
    public MessageUpdateDiskFrameItemStack decode(PacketBuffer buffer) {
        return new MessageUpdateDiskFrameItemStack(buffer.readItemStack(), buffer.readItemStack(), buffer.readEnumValue(Hand.class),buffer.readUniqueId());
    }

    @Override
    public void handle(MessageUpdateDiskFrameItemStack message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                message.stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    h.insertItem(0, message.frame, false);
                });
                Minecraft.
                        getInstance().
                        world.
                        getPlayerByUuid(message.uuid).
                        setHeldItem(message.hand, message.stack);
            });
        });
        supplier.get().

    setPacketHandled(true);
}
}
