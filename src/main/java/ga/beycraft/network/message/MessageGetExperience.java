package ga.beycraft.network.message;

import ga.beycraft.common.capability.entity.BladerCapabilityProvider;
import ga.beycraft.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageGetExperience implements IMessage<MessageGetExperience>{
    @Override
    public void encode(MessageGetExperience message, PacketBuffer buffer) {

    }

    @Override
    public MessageGetExperience decode(PacketBuffer buffer) {
        return new MessageGetExperience();
    }

    @Override
    public void handle(MessageGetExperience message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(cap->{
                cap.getBladerLevel().setExperience(ClientUtils.RankingUtils.getExperience());
                cap.syncToAll();
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
