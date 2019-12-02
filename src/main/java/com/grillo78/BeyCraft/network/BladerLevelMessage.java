package com.grillo78.BeyCraft.network;

import com.grillo78.BeyCraft.BeyCraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BladerLevelMessage implements IMessage, IMessageHandler<BladerLevelMessage, IMessage> {

	public EntityPlayerMP player;

	public BladerLevelMessage() {
	}

	public BladerLevelMessage(EntityPlayerMP player) {
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public IMessage onMessage(BladerLevelMessage message, MessageContext ctx) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;


		if (player != null) {
			Minecraft.getMinecraft().addScheduledTask((Runnable) () -> {
//				player.getCapability(Provider.BLADERLEVEL_CAP, null)
//						.setBladerLevel(message.player.getCapability(Provider.BLADERLEVEL_CAP, null).getBladerLevel());
				BeyCraft.logger.info("Test message");
			});
		}
		return null;
	}
}
