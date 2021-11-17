package ga.beycraft.network.message;

import ga.beycraft.BeyCraftRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageNotifyPlayCountdown implements IMessage<MessageNotifyPlayCountdown> {

    @Override
    public void encode(MessageNotifyPlayCountdown message, PacketBuffer buffer) {
    }

    @Override
    public MessageNotifyPlayCountdown decode(PacketBuffer buffer) {
        return new MessageNotifyPlayCountdown();
    }

    @Override
    public void handle(MessageNotifyPlayCountdown message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity serverPlayerEntity = supplier.get().getSender();
            serverPlayerEntity.getLevel().playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), BeyCraftRegistry.COUNTDOWN, SoundCategory.PLAYERS, 1, 1);
        });
        supplier.get().setPacketHandled(true);
    }
}
