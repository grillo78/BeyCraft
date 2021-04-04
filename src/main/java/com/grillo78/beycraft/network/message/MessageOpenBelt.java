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
import xyz.heroesunited.heroesunited.common.capabilities.HUPlayer;
import xyz.heroesunited.heroesunited.common.capabilities.IHUPlayer;
import xyz.heroesunited.heroesunited.common.objects.container.AccessoriesInventory;
import xyz.heroesunited.heroesunited.common.objects.container.EquipmentAccessoriesSlot;

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
            IHUPlayer cap = HUPlayer.getCap(player);

            AccessoriesInventory h = cap.getInventory();


                ItemStack stack = h.getItem(EquipmentAccessoriesSlot.BELT.getSlot());
                if (stack.getItem() instanceof ItemBladerBelt) {
                    player.playNotifySound(BeyRegistry.OPEN_CLOSE_BELT, SoundCategory.PLAYERS, 1, 1);
                    NetworkHooks.openGui(player,
                            new SimpleNamedContainerProvider(
                                    (id, playerInventory, playerEntity) -> new BeltContainer(BeyRegistry.BELT_CONTAINER, id,
                                            stack, playerInventory, false),
                                    new StringTextComponent(stack.getItem().getRegistryName().getPath())));
                }
        });
        supplier.get().setPacketHandled(true);
    }
}
