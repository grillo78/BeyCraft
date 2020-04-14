package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.items.ItemDualLauncher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateDualLauncher implements IMessage<MessageUpdateDualLauncher> {

    private int rotation;

    public MessageUpdateDualLauncher() {
    }

    public MessageUpdateDualLauncher(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public void encode(MessageUpdateDualLauncher message, PacketBuffer buffer) {
        buffer.writeInt(message.rotation);
    }

    @Override
    public MessageUpdateDualLauncher decode(PacketBuffer buffer) {
        return new MessageUpdateDualLauncher(buffer.readInt());
    }

    @Override
    public void handle(MessageUpdateDualLauncher message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ItemStack launcher;
            if (supplier.get().getSender().getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemDualLauncher) {
                launcher = supplier.get().getSender().getHeldItem(Hand.MAIN_HAND);
            } else {
                launcher = supplier.get().getSender().getHeldItem(Hand.OFF_HAND);
            }
            if (!launcher.hasTag()) {
                CompoundNBT compound = new CompoundNBT();
                compound.putInt("rotation", 1);
                launcher.setTag(compound);
            }
            CompoundNBT compound = launcher.getTag();
            compound.putInt("rotation", message.rotation);
            launcher.setTag(compound);

        });
        supplier.get().setPacketHandled(true);
    }
}
