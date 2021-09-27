package ga.beycraft.network.message;

import ga.beycraft.network.PacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
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
			for (ServerPlayerEntity player: supplier.get().getSender().getLevel().players()) {
				PacketHandler.instance.sendTo(new MessagePlayCountdown(supplier.get().getSender().blockPosition()),player.connection.getConnection(),
						NetworkDirection.PLAY_TO_CLIENT);
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
