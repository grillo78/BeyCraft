package ga.beycraft.network.message;

import ga.beycraft.BeyCraftRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSpawnSpark implements IMessage<MessageSpawnSpark> {

    private int entityID;
    private float speedX;
    private float speedY;
    private float speedZ;

    public MessageSpawnSpark(){}

    public MessageSpawnSpark(int entityID, float speedX, float speedY, float speedZ){
        this.entityID = entityID;
        this.speedX = speedX;
        this.speedY = speedY;
        this.speedZ = speedZ;
    }

    @Override
    public void encode(MessageSpawnSpark message, PacketBuffer buffer) {
        buffer.writeInt(message.entityID);
        buffer.writeFloat(message.speedX);
        buffer.writeFloat(message.speedY);
        buffer.writeFloat(message.speedZ);
    }

    @Override
    public MessageSpawnSpark decode(PacketBuffer buffer) {
        return new MessageSpawnSpark(buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    @Override
    public void handle(MessageSpawnSpark message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{

            Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
            Vector3d position = entity.getPosition(Minecraft.getInstance().getFrameTime());

            Minecraft.getInstance().level.addParticle(BeyCraftRegistry.SPARKLE, position.x, position.y,position.z, message.speedX, message.speedY, message.speedZ);
        });
        supplier.get().setPacketHandled(true);
    }
}
