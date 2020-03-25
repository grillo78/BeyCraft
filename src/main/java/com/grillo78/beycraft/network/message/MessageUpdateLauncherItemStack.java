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

public class MessageUpdateLauncherItemStack implements IMessage<MessageUpdateLauncherItemStack> {

    private ItemStack stack;
    private ItemStack layer;
    private ItemStack handle;
    private ItemStack beylogger;
    private Hand hand;
    private UUID uuid;

    public MessageUpdateLauncherItemStack() {
    }

    public MessageUpdateLauncherItemStack(ItemStack stack, ItemStack layer, ItemStack handle, ItemStack beylogger, Hand hand, UUID uuid) {
        this.stack = new ItemStack(stack.getItem());
        this.layer = layer;
        this.handle = handle;
        this.beylogger = beylogger;
        this.hand = hand;
    }

    @Override
    public void encode(MessageUpdateLauncherItemStack message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
        buffer.writeItemStack(message.layer);
        buffer.writeItemStack(message.handle);
        buffer.writeItemStack(message.beylogger);
        buffer.writeEnumValue(message.hand);
        buffer.writeUniqueId(message.uuid);
    }

    @Override
    public MessageUpdateLauncherItemStack decode(PacketBuffer buffer) {
        return new MessageUpdateLauncherItemStack(buffer.readItemStack(), buffer.readItemStack(), buffer.readItemStack(), buffer.readItemStack(), buffer.readEnumValue(Hand.class),buffer.readUniqueId());
    }

    @Override
    public void handle(MessageUpdateLauncherItemStack message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                message.stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    h.insertItem(0, message.layer, false);
                    h.insertItem(1, message.handle, false);
                    h.insertItem(3, message.handle, false);
                });
                Minecraft.
                        getInstance().
                        world.
                        getPlayerByUuid(message.uuid).
                        setHeldItem(message.hand, message.stack);
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
