package ga.beycraft.network.message;

import ga.beycraft.util.RankingUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageLoseCombat implements IMessage<MessageLoseCombat>{

    @Override
    public void encode(MessageLoseCombat message, PacketBuffer buffer) {

    }

    @Override
    public MessageLoseCombat decode(PacketBuffer buffer) {
        return new MessageLoseCombat();
    }

    @Override
    public void handle(MessageLoseCombat message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            RankingUtil.loseCombat();
        });
        supplier.get().setPacketHandled(true);
    }
}
