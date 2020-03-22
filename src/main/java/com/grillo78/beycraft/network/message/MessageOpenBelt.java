package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.inventory.BeltContainer;
import com.grillo78.beycraft.items.ItemBladerBelt;
import com.lazy.baubles.api.BaubleType;
import com.lazy.baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class MessageOpenBelt implements IMessage<MessageOpenBelt> {
    @Override
    public void encode(MessageOpenBelt message, PacketBuffer buffer) {

    }

    @Override
    public MessageOpenBelt decode(PacketBuffer buffer) {
        return new MessageOpenBelt();
    }

    @Override
    public void handle(MessageOpenBelt message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            ServerPlayerEntity player = supplier.get().getSender();
            player.getCapability(BaublesCapabilities.BAUBLES).ifPresent(h -> {
                ItemStack stack = h.getStackInSlot(BaubleType.BELT.getValidSlots()[0]);
                if(stack.getItem() instanceof ItemBladerBelt){
                    NetworkHooks.openGui( player,
                            new SimpleNamedContainerProvider(
                                    (id, playerInventory, playerEntity) -> new BeltContainer(BeyRegistry.BELT_CONTAINER, id,
                                            stack, playerInventory),
                                    new StringTextComponent(stack.getItem().getRegistryName().getPath())));
                }
            });

        });
        supplier.get().setPacketHandled(true);
    }
}
