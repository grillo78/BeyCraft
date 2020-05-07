package com.grillo78.beycraft.network;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel instance;
    private static int nextId = 0;

    public static void init()
    {
        instance = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Reference.MODID, "network"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();
        register(MessageOpenBelt.class, new MessageOpenBelt());
        register(MessageBeyCreatorUpdate.class, new MessageBeyCreatorUpdate());
        register(MessageUpdateDualLauncher.class, new MessageUpdateDualLauncher());
        register(MessageUpdateColorLauncher.class, new MessageUpdateColorLauncher());
        register(MessageSyncBladerLevel.class, new MessageSyncBladerLevel());
        register(MessageUpdateUrlBeyLogger.class, new MessageUpdateUrlBeyLogger());
    }

    private static <T> void register(Class<T> clazz, IMessage<T> message)
    {
        instance.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
}
