package ga.beycraft.network.message;

import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.network.PacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageEnableResonance implements IMessage<MessageEnableResonance> {

    public MessageEnableResonance() {
    }

    @Override
    public void encode(MessageEnableResonance message, PacketBuffer buffer) {
    }

    @Override
    public MessageEnableResonance decode(PacketBuffer buffer) {
        return new MessageEnableResonance();
    }

    @Override
    public void handle(MessageEnableResonance message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            supplier.get().getSender().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
                if (h.getBladerLevel() >= 15) {
                    h.setInResonance(true);
                    supplier.get().getSender().level.players().forEach(playerEntity -> PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getExperience(), h.isInResonance(), false, supplier.get().getSender().getId()),
                            ((ServerPlayerEntity)playerEntity).connection.getConnection(),
                            NetworkDirection.PLAY_TO_CLIENT));
                }
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
