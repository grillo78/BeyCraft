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

public class MessageUpdateHandleItemStack implements IMessage<MessageUpdateHandleItemStack> {

    private ItemStack stack;
    private ItemStack slot1;
    private ItemStack slot2;
    private ItemStack slot3;
    private Hand hand;
    private UUID uuid;

    public MessageUpdateHandleItemStack() {
    }

    public MessageUpdateHandleItemStack(ItemStack stack, ItemStack slot1, ItemStack slot2, ItemStack slot3, Hand hand, UUID uuid) {
        this.stack = new ItemStack(stack.getItem());
        this.slot1 = slot1;
        this.slot2 = slot2;
        this.slot3 = slot3;
        this.hand = hand;
    }

    @Override
    public void encode(MessageUpdateHandleItemStack message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
        buffer.writeItemStack(message.slot1);
        buffer.writeItemStack(message.slot2);
        buffer.writeItemStack(message.slot3);
        buffer.writeEnumValue(message.hand);
        buffer.writeUniqueId(message.uuid);
    }

    @Override
    public MessageUpdateHandleItemStack decode(PacketBuffer buffer) {
        return new MessageUpdateHandleItemStack(buffer.readItemStack(), buffer.readItemStack(), buffer.readItemStack(), buffer.readItemStack(), buffer.readEnumValue(Hand.class),buffer.readUniqueId());
    }

    @Override
    public void handle(MessageUpdateHandleItemStack message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                message.stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    h.insertItem(0, message.slot1, false);
                    h.insertItem(1, message.slot2, false);
                    h.insertItem(3, message.slot2, false);
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
