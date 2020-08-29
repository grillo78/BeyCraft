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
import net.minecraftforge.fml.network.NetworkDirection;

public class SetLevelCommand {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("BladerLevel")
				.requires((p_198485_0_) -> p_198485_0_.hasPermissionLevel(2)).then(Commands.literal("set")
						.then(Commands.argument("targets", EntityArgument.players())
								.then(Commands.argument("amount", IntegerArgumentType.integer(0))
										.executes(context -> setLevel(context.getSource(), EntityArgument.getPlayers(context, "targets"),
												IntegerArgumentType.getInteger(context, "amount"))))));
		literalargumentbuilder.then(Commands.literal("add")
				.then(Commands.argument("targets", EntityArgument.players())
						.then(Commands.argument("amount", IntegerArgumentType.integer(0))
								.executes(context -> addLevels(context.getSource(), EntityArgument.getPlayers(context, "targets"),
										IntegerArgumentType.getInteger(context, "amount"))))));
		literalargumentbuilder.then(Commands.literal("remove")
				.then(Commands.argument("targets", EntityArgument.players())
						.then(Commands.argument("amount", IntegerArgumentType.integer(0))
								.executes(context -> removeLevels(context.getSource(), EntityArgument.getPlayers(context, "targets"),
										IntegerArgumentType.getInteger(context, "amount"))))));
		dispatcher.register(literalargumentbuilder);
	}

	private static int setLevel(CommandSource source, Collection<? extends ServerPlayerEntity> targets, int amount) {
		int i = 0;
		for (ServerPlayerEntity player : targets) {
			player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h->{
				h.setBladerLevel(amount);
				h.setExperience(0);
			});
			PacketHandler.instance.sendTo(new MessageSyncBladerLevel(amount,0),player.connection.getNetworkManager(),
					NetworkDirection.PLAY_TO_CLIENT);
		}
		return i;
	}

	private static int addLevels(CommandSource source, Collection<? extends ServerPlayerEntity> targets, int amount) {
		int i = 0;
		for (ServerPlayerEntity player : targets) {
			player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h->{
				h.setBladerLevel(h.getBladerLevel()+amount);
				h.setExperience(0);
				PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getBladerLevel(),0),player.connection.getNetworkManager(),
						NetworkDirection.PLAY_TO_CLIENT);
			});
		}
		return i;
	}

	private static int removeLevels(CommandSource source, Collection<? extends ServerPlayerEntity> targets, int amount) {
		int i = 0;
		for (ServerPlayerEntity player : targets) {
			player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h->{
				h.setBladerLevel(h.getBladerLevel()-amount);
				h.setExperience(0);
				PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getBladerLevel(),0),player.connection.getNetworkManager(),
						NetworkDirection.PLAY_TO_CLIENT);
			});
		}
		return i;
	}
}
