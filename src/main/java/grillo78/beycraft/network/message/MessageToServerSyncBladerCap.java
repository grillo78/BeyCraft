package grillo78.beycraft.network.message;

import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import grillo78.beycraft.network.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageToServerSyncBladerCap implements IMessage<MessageToServerSyncBladerCap>{

    private CompoundNBT nbt;

    public MessageToServerSyncBladerCap() {
    }

    public MessageToServerSyncBladerCap(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    @Override
    public void encode(MessageToServerSyncBladerCap message, PacketBuffer buffer) {
        buffer.writeNbt(message.nbt);
    }

    @Override
    public MessageToServerSyncBladerCap decode(PacketBuffer buffer) {
        return new MessageToServerSyncBladerCap(buffer.readAnySizeNbt());
    }

    @Override
    public void handle(MessageToServerSyncBladerCap message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            PlayerEntity player = supplier.get().getSender();
            player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h->{
                h.readNetwork(message.nbt);
                for (ServerPlayerEntity playerAux: supplier.get().getSender().getLevel().players()) {
                    PacketHandler.INSTANCE.sendTo(new MessageSyncBladerCap(message.nbt,player.getId()),playerAux.connection.getConnection(),
                            NetworkDirection.PLAY_TO_CLIENT);
                }
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
