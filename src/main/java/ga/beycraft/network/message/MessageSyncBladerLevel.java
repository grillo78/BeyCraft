package ga.beycraft.network.message;

import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.util.RankingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncBladerLevel implements IMessage<MessageSyncBladerLevel> {

    private float experience;
    private boolean inResonance = false;
    private boolean syncWithWeb = false;
    private int playerID;

    public MessageSyncBladerLevel(){}

    public MessageSyncBladerLevel(float experience, boolean inResonance, boolean syncWithWeb, int playerID){
        this.experience = experience;
        this.inResonance = inResonance;
        this.syncWithWeb = syncWithWeb;
        this.playerID = playerID;
    }

    @Override
    public void encode(MessageSyncBladerLevel message, PacketBuffer buffer) {
        buffer.writeFloat(message.experience);
        buffer.writeBoolean(message.inResonance);
        buffer.writeBoolean(message.syncWithWeb);
        buffer.writeInt(message.playerID);
    }

    @Override
    public MessageSyncBladerLevel decode(PacketBuffer buffer) {
        return new MessageSyncBladerLevel(buffer.readFloat(), buffer.readBoolean(), buffer.readBoolean(), buffer.readInt());
    }

    @Override
    public void handle(MessageSyncBladerLevel message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            Minecraft.getInstance().level.getEntity(message.playerID).getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h->{
                if(message.syncWithWeb)
                    RankingUtil.increaseExperience(message.experience - h.getExperience());
                h.setExperience(message.experience);
                h.setInResonance(message.inResonance);
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
