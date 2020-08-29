package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.tileentity.RobotTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSetRobotLevel implements IMessage<MessageSetRobotLevel> {

	private BlockPos pos;
	private int bladerLevel;

	public MessageSetRobotLevel() {
	}

	public MessageSetRobotLevel(BlockPos pos, int bladerLevel) {
		this.pos = pos;
		this.bladerLevel = bladerLevel;
	}

	@Override
	public void encode(MessageSetRobotLevel message, PacketBuffer buffer) {
		buffer.writeInt(message.pos.getX());
		buffer.writeInt(message.pos.getY());
		buffer.writeInt(message.pos.getZ());
		buffer.writeInt(message.bladerLevel);
	}

	@Override
	public MessageSetRobotLevel decode(PacketBuffer buffer) {
		return new MessageSetRobotLevel(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()),
				buffer.readInt());
	}

	@Override
	public void handle(MessageSetRobotLevel message, Supplier<NetworkEvent.Context> supplier) {
		supplier.get().enqueueWork(()->{
			((RobotTileEntity)supplier.get().getSender().world.getTileEntity(message.pos)).setBladerLevel(message.bladerLevel);
		});
		supplier.get().setPacketHandled(true);
	}
}
