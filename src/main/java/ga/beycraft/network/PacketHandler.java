package ga.beycraft.network;

import ga.beycraft.Reference;
import ga.beycraft.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = Reference.VERSION;

    public static SimpleChannel instance;
    private static int nextId = 0;

    public static void init()
    {
        instance = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Reference.MOD_ID, "beycraft_network"))
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
        register(MessageNotifyPlayCountdown.class, new MessageNotifyPlayCountdown());
        register(MessageOpenRobotGUI.class, new MessageOpenRobotGUI());
        register(MessageSetRobotLevel.class, new MessageSetRobotLevel());
        register(MessageWinCombat.class, new MessageWinCombat());
        register(MessageLoseCombat.class, new MessageLoseCombat());
        register(MessageSetBladerExperience.class, new MessageSetBladerExperience());
        register(MessageGetExperience.class, new MessageGetExperience());
        register(MessageSpawnSpark.class, new MessageSpawnSpark());
        register(MessageEnableResonance.class, new MessageEnableResonance());
    }

    private static <T> void register(Class<T> clazz, IMessage<T> message)
    {
        instance.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
}
