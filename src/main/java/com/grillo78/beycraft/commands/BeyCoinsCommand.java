package com.grillo78.beycraft.commands;

import java.util.Collection;

import com.grillo78.beycraft.capabilities.BladerCapProvider;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageSyncBladerLevel;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;

public class BeyCoinsCommand {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("BeyCoins").executes((context -> messageBeyCoins(context.getSource())));
		dispatcher.register(literalargumentbuilder);
	}

	private static int messageBeyCoins(CommandSource source) {
		int i = 0;
		if(source.getEntity() != null && source.getEntity() instanceof ServerPlayerEntity){
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			player.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h->{
				source.sendFeedback(new TranslationTextComponent("beycoins.command.message",h.getCurrency()),true);
			});
		}
		return i;
	}
}
