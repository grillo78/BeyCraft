package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.inventory.BeltContainer;
import com.grillo78.beycraft.items.ItemBladerBelt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.function.Supplier;

public class MessageOpenBelt implements IMessage<MessageOpenBelt> {
    @Override
    public void encode(MessageOpenBelt message, PacketBuffer buffer) {}

    @Override
    public MessageOpenBelt decode(PacketBuffer buffer) {
        return new MessageOpenBelt();
    }

    @Override
    public void handle(MessageOpenBelt message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            ServerPlayerEntity player = supplier.get().getSender();
            CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(h -> {
                for (int i = 0; i < h.getSlots(); i++) {
                    ItemStack stack = h.getStackInSlot(i);
                    if (stack.getItem() instanceof ItemBladerBelt) {
                        player.playSound(BeyRegistry.OPEN_CLOSE_BELT, SoundCategory.PLAYERS, 1, 1);
                        NetworkHooks.openGui(player,
                                new SimpleNamedContainerProvider(
                                        (id, playerInventory, playerEntity) -> new BeltContainer(BeyRegistry.BELT_CONTAINER, id,
                                                stack, playerInventory, false),
                                        new StringTextComponent(stack.getItem().getRegistryName().getPath())));
                    }
                }
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
