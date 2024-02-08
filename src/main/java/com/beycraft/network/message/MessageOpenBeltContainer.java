package com.beycraft.network.message;

import com.beycraft.common.container.BeltContainer;
import com.beycraft.common.container.ModContainers;
import com.beycraft.common.item.BladerBelt;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.heroesunited.heroesunited.common.capabilities.HUPlayer;
import xyz.heroesunited.heroesunited.common.objects.container.EquipmentAccessoriesSlot;

import java.util.function.Supplier;

public class MessageOpenBeltContainer implements IMessage<MessageOpenBeltContainer> {
    @Override
    public void encode(MessageOpenBeltContainer message, PacketBuffer buffer) {

    }

    @Override
    public MessageOpenBeltContainer decode(PacketBuffer buffer) {
        return new MessageOpenBeltContainer();
    }

    @Override
    public void handle(MessageOpenBeltContainer message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = supplier.get().getSender();
            if (HUPlayer.getCap(player).getInventory().getItem(EquipmentAccessoriesSlot.BELT.getSlot()).getItem() instanceof BladerBelt)
            NetworkHooks.openGui(player,
                    new SimpleNamedContainerProvider((id, playerInventory, playerEntity) -> new BeltContainer(ModContainers.BELT_CONTAINER, id, HUPlayer.getCap(playerEntity).getInventory().getItem(EquipmentAccessoriesSlot.BELT.getSlot()), playerInventory), StringTextComponent.EMPTY));
        });
    }
}
