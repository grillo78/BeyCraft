package ga.beycraft.network.message;

import ga.beycraft.util.RankingUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageWinCombat implements IMessage<MessageWinCombat>{

    @Override
    public void encode(MessageWinCombat message, PacketBuffer buffer) {

    }

    @Override
    public MessageWinCombat decode(PacketBuffer buffer) {
        return new MessageWinCombat();
    }

    @Override
    public void handle(MessageWinCombat message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            RankingUtil.winCombat();
        });
        supplier.get().setPacketHandled(true);
    }
}
