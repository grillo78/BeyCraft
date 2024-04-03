package grillo78.beycraft.common.commands;

import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class BeyCoinsCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("BeyCoins").executes((context -> messageBeyCoins(context.getSource())));
        dispatcher.register(literalargumentbuilder);
    }

    private static int messageBeyCoins(CommandSource source) {
        if(source.getEntity() != null && source.getEntity() instanceof ServerPlayerEntity){
            ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
            player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h->{
                source.sendSuccess(new TranslationTextComponent("beycoins.command.message",h.getWallet().getCurrency()),true);
            });
        }
        return 0;
    }
}
