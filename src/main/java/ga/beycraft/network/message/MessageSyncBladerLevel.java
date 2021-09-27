package ga.beycraft.network.message;

import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.util.RankingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncBladerLevel implements IMessage<MessageSyncBladerLevel> {

    private float experience;

    public MessageSyncBladerLevel(){}

    public MessageSyncBladerLevel(float experience){
        this.experience = experience;
    }

    @Override
    public void encode(MessageSyncBladerLevel message, PacketBuffer buffer) {
        buffer.writeFloat(message.experience);
    }

    @Override
    public MessageSyncBladerLevel decode(PacketBuffer buffer) {
        return new MessageSyncBladerLevel(buffer.readFloat());
    }

    @Override
    public void handle(MessageSyncBladerLevel message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            Minecraft.getInstance().player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h->{
                h.setExperience(message.experience);
                RankingUtil.updateExperience(message.experience);
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
