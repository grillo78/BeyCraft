package com.grillo78.beycraft.network.message;

import java.util.function.Supplier;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.capabilities.BladerLevelProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessagePlayCountdown implements IMessage<MessagePlayCountdown> {

	@Override
	public void encode(MessagePlayCountdown message, PacketBuffer buffer) {
	}

	@Override
	public MessagePlayCountdown decode(PacketBuffer buffer) {
		return new MessagePlayCountdown();
	}

	@Override
	public void handle(MessagePlayCountdown message, Supplier<NetworkEvent.Context> supplier) {
		supplier.get().enqueueWork(() -> {
			supplier.get().getSender().playSound(BeyRegistry.COUNTDOWN, SoundCategory.PLAYERS, 1, 1);
		});
		supplier.get().setPacketHandled(true);
	}
}
