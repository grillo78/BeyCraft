package ga.beycraft.network.message;

import ga.beycraft.BeyCraftRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessagePlayCountdown implements IMessage<MessagePlayCountdown> {

	private BlockPos pos;
	private boolean isValid = false;

	public MessagePlayCountdown() {
	}

	public MessagePlayCountdown(BlockPos pos) {
		this.pos = pos;
		isValid = true;
	}

	@Override
	public void encode(MessagePlayCountdown message, PacketBuffer buffer) {
		buffer.writeInt(message.pos.getX());
		buffer.writeInt(message.pos.getY());
		buffer.writeInt(message.pos.getZ());
	}

	@Override
	public MessagePlayCountdown decode(PacketBuffer buffer) {
		return new MessagePlayCountdown(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
	}

	@Override
	public void handle(MessagePlayCountdown message, Supplier<NetworkEvent.Context> supplier) {
		supplier.get().enqueueWork(() -> {
			if (message.isValid) {
				Minecraft.getInstance().level.playLocalSound(message.pos, BeyCraftRegistry.COUNTDOWN, SoundCategory.PLAYERS, 1, 1,
						true);
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
