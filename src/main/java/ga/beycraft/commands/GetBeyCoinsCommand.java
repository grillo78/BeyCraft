package ga.beycraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.capabilities.BladerCapProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class GetBeyCoinsCommand {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("Get_BeyCoins").then(Commands.argument("count", IntegerArgumentType.integer(1)).executes((context -> giveBeyCoin(context.getSource(), IntegerArgumentType.getInteger(context, "count")))));
		dispatcher.register(literalargumentbuilder);
	}

	private static int giveBeyCoin(CommandSource source, int amount) {
		int i = 0;
		if(source.getEntity() != null && source.getEntity() instanceof ServerPlayerEntity){
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			player.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h->{
				if(h.getCurrency()>=amount){
					h.increaseCurrency(-amount);
					player.level.addFreshEntity(new ItemEntity(player.level,player.getX(),
							player.getY(), player.getZ(), new ItemStack(BeyCraftRegistry.BEYCOIN, amount)));
					source.sendSuccess(new TranslationTextComponent("beycoins.successful.command.message", amount),true);
				} else {
					source.sendFailure(new TranslationTextComponent("beycoins.fail.command.message"));
				}
			});
		}
		return i;
	}
}
