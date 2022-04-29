package ga.beycraft.network;

import ga.beycraft.Beycraft;
import ga.beycraft.network.message.IMessage;
import ga.beycraft.network.message.MessageSyncBladerCap;
import ga.beycraft.network.message.MessageToServerSyncBladerCap;
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
    }

    /**
     * Method to register a packet
     *
     * @param clazz   Class of the packet
     * @param message Message object
     * @param <T>
     */
    private static <T> void register(Class<T> clazz, IMessage<T> message) {
        INSTANCE.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
}
