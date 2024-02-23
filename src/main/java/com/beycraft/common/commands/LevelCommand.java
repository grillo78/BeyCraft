package com.beycraft.common.commands;

import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class LevelCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = Commands.literal("Bladerlevel").requires(source-> source.hasPermission(2)).then(Commands.argument("level", IntegerArgumentType.integer(1)).executes((context -> setLevel(context.getSource(), IntegerArgumentType.getInteger(context, "level")))));
        dispatcher.register(literalArgumentBuilder);
    }

    private static int setLevel(CommandSource source, int level) {
        int i = 0;
        if(source.getEntity() != null && source.getEntity() instanceof ServerPlayerEntity){
            ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
            player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h->{
                h.getBladerLevel().setCustomLevel(level);
                h.syncToAll();
            });
        }
        return i;
    }
}
