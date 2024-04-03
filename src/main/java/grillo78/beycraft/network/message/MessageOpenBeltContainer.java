package grillo78.beycraft.network.message;

import grillo78.beycraft.common.container.BeltContainer;
import grillo78.beycraft.common.container.ModContainers;
import grillo78.beycraft.common.item.BladerBelt;
import grillo78.clothes_mod.common.capabilities.ClothesProvider;
import grillo78.clothes_mod.common.items.ClothesSlot;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

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
            player.getCapability(ClothesProvider.CLOTHES_INVENTORY).ifPresent(iClothesInvWrapper -> {
                if (iClothesInvWrapper.getInventory().getStackInSlot(ClothesSlot.BELT.getID()).getItem() instanceof BladerBelt)
                    NetworkHooks.openGui(player,
                            new SimpleNamedContainerProvider((id, playerInventory, playerEntity) -> new BeltContainer(ModContainers.BELT_CONTAINER, id, iClothesInvWrapper.getInventory().getStackInSlot(ClothesSlot.BELT.getID()), playerInventory), StringTextComponent.EMPTY));
            });
        });
    }
}
