package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.items.ItemDualLauncher;
import com.grillo78.beycraft.items.ItemLauncher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateColorLauncher implements IMessage<MessageUpdateColorLauncher> {

    private float red;
    private float green;
    private float blue;

    public MessageUpdateColorLauncher() {
    }

    public MessageUpdateColorLauncher(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public void encode(MessageUpdateColorLauncher message, PacketBuffer buffer) {
        buffer.writeFloat(message.red);
        buffer.writeFloat(message.green);
        buffer.writeFloat(message.blue);
    }

    @Override
    public MessageUpdateColorLauncher decode(PacketBuffer buffer) {
        return new MessageUpdateColorLauncher(buffer.readFloat(),buffer.readFloat(),buffer.readFloat());
    }

    @Override
    public void handle(MessageUpdateColorLauncher message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ItemStack launcher;
            if (supplier.get().getSender().getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemLauncher) {
                launcher = supplier.get().getSender().getItemInHand(Hand.MAIN_HAND);
            } else {
                launcher = supplier.get().getSender().getItemInHand(Hand.OFF_HAND);
            }
            if (!launcher.hasTag()) {
                CompoundNBT compound = new CompoundNBT();
                launcher.setTag(compound);
            }
            CompoundNBT compound = launcher.getTag();
            CompoundNBT colorNBT = new CompoundNBT();
            colorNBT.putFloat("red",message.red);
            colorNBT.putFloat("green",message.green);
            colorNBT.putFloat("blue",message.blue);
            compound.put("color", colorNBT);
            launcher.setTag(compound);

        });
        supplier.get().setPacketHandled(true);
    }
}
