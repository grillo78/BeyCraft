package grillo78.beycraft.network;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel INSTANCE;
    private static int nextId = 0;

    /**
     * create the network channel and register the packets
     */
    public static void init() {
        // Create the Network channel
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Beycraft.MOD_ID, "network"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();

        // Register packets
        register(MessageSyncBladerCap.class, new MessageSyncBladerCap());
        register(MessageToServerSyncBladerCap.class, new MessageToServerSyncBladerCap());
        register(MessageActivateLaunch.class, new MessageActivateLaunch());
        register(MessageActivateResonance.class, new MessageActivateResonance());
        register(MessageApplyAnimation.class, new MessageApplyAnimation());
        register(MessageOpenBeltContainer.class, new MessageOpenBeltContainer());
        register(MessageHandSpinning.class, new MessageHandSpinning());
        register(UpdateLauncherColor.class, new UpdateLauncherColor());
    }
    
    private static <T> void register(Class<T> clazz, IMessage<T> message) {
        INSTANCE.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
}
