package ga.beycraft.network.message;

import ga.beycraft.network.PacketHandler;
import ga.beycraft.util.RankingUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageGetExperience implements IMessage<MessageGetExperience>{

    @Override
    public void encode(MessageGetExperience message, PacketBuffer buffer) {}

    @Override
    public MessageGetExperience decode(PacketBuffer buffer) {
        return new MessageGetExperience();
    }

    @Override
    public void handle(MessageGetExperience message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            PacketHandler.instance.sendToServer(new MessageSetBladerExperience(RankingUtil.getExperience()));
        });
        supplier.get().setPacketHandled(true);
    }
}
