package ga.beycraft.network.message;

import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.network.PacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSetBladerExperience implements IMessage<MessageSetBladerExperience> {

    private float experience;

    public MessageSetBladerExperience(){}

    public MessageSetBladerExperience(float experience){
        this.experience = experience;
    }

    @Override
    public void encode(MessageSetBladerExperience message, PacketBuffer buffer) {
        buffer.writeFloat(message.experience);
    }

    @Override
    public MessageSetBladerExperience decode(PacketBuffer buffer) {
        return new MessageSetBladerExperience(buffer.readFloat());
    }

    @Override
    public void handle(MessageSetBladerExperience message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            supplier.get().getSender().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h->{
                h.setExperience(message.experience);
                PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getExperience()),
                        supplier.get().getSender().connection.getConnection(),
                        NetworkDirection.PLAY_TO_CLIENT);
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
