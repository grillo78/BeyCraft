package com.grillo78.BeyCraft.network;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.capabilities.Provider;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class BladerLevelMessage implements IMessage, IMessageHandler<BladerLevelMessage, IMessage> {

	private int bladerLevel;
	private boolean messageValid;

	public BladerLevelMessage() {
		messageValid = false;
	}

	public BladerLevelMessage(int bladerLevel) {
		this.bladerLevel = bladerLevel;
		messageValid = true;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			bladerLevel = buf.readInt();
		} catch (IndexOutOfBoundsException ioe) {
			BeyCraft.logger.catching(ioe);
			return;
		}
		this.messageValid = true;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) {
			return;
		}
		buf.writeInt(bladerLevel);

	}

	public int getBladerLevel() {
		return bladerLevel;
	}

	@Override
	public IMessage onMessage(BladerLevelMessage message, MessageContext ctx) {
		// This is the player the packet was sent to the server from
		Minecraft.getMinecraft().player.getCapability(Provider.BLADERLEVEL_CAP, null)
				.setBladerLevel(message.bladerLevel);
		BeyCraft.logger.info("Test");
		// No response packet
		return null;
	}
}
