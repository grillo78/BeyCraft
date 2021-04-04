package com.grillo78.beycraft.commands;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.capabilities.BladerCapProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class BeyBoxCommand {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("BeyBox").executes((context -> giveBeyBox(context.getSource())));
		dispatcher.register(literalargumentbuilder);
	}

	private static int giveBeyBox(CommandSource source) {
		int i = 0;
		if(source.getEntity() != null && source.getEntity() instanceof ServerPlayerEntity){
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			player.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h->{
				if(h.getCurrency()>1000){
					h.increaseCurrency(-1000);
					player.level.addFreshEntity(new ItemEntity(player.level,player.getX(),
							player.getY(), player.getZ(), new ItemStack(BeyRegistry.BEYPACKAGE)));
					source.sendSuccess(new TranslationTextComponent("beybox.successful.command.message"),true);
				} else {
					source.sendFailure(new TranslationTextComponent("beybox.fail.command.message"));
				}
			});
		}
		return i;
	}
}
