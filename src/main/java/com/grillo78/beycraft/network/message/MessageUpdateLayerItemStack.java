package com.grillo78.beycraft.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageUpdateLayerItemStack implements IMessage<MessageUpdateLayerItemStack> {

    private ItemStack stack;
    private ItemStack disc;
    private ItemStack driver;
    private Hand hand;
    private UUID uuid;

    public MessageUpdateLayerItemStack(){}

    public MessageUpdateLayerItemStack(ItemStack stack, ItemStack disc, ItemStack driver, Hand hand, UUID uuid){
        this.stack = new ItemStack(stack.getItem());
        this.disc = disc;
        this.driver = driver;
        this.hand = hand;
        this.uuid = uuid;
    }

    @Override
    public void encode(MessageUpdateLayerItemStack message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
        buffer.writeItemStack(message.disc);
        buffer.writeItemStack(message.driver);
        buffer.writeEnumValue(message.hand);
        buffer.writeUniqueId(message.uuid);
    }

    @Override
    public MessageUpdateLayerItemStack decode(PacketBuffer buffer) {
        return new MessageUpdateLayerItemStack(buffer.readItemStack(),buffer.readItemStack(),buffer.readItemStack(),buffer.readEnumValue(Hand.class), buffer.readUniqueId());
    }

    @Override
    public void handle(MessageUpdateLayerItemStack message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() ->{
            DistExecutor.runWhenOn(Dist.CLIENT,()->()->{
                message.stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
                    h.insertItem(0,message.disc,false);
                    h.insertItem(1,message.driver,false);
                });
                if(!message.stack.hasTag()){
                    CompoundNBT nbt = new CompoundNBT();
                    message.stack.setTag(nbt);
                }
                CompoundNBT nbt = message.stack.getTag();
                nbt.put("disc", message.disc.write(new CompoundNBT()));
                nbt.put("driver", message.driver.write(new CompoundNBT()));
                Minecraft.
                        getInstance().
                        world.
                        getPlayerByUuid(message.uuid).
                        setHeldItem(message.hand,message.stack);
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
