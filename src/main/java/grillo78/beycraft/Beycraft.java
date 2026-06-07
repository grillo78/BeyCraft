package grillo78.beycraft;

import com.mojang.logging.LogUtils;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.entities.Beyblade;
import grillo78.beycraft.entities.ModEntities;
import grillo78.beycraft.items.ModItems;
import grillo78.beycraft.items.components.ModDataComponents;
import grillo78.beycraft.network.SyncBeyparts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Beycraft.MOD_ID)
public class Beycraft {

    public static final String MOD_ID = "beycraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Beycraft(IEventBus modEventBus, ModContainer modContainer) {
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::registerPackets);
        modEventBus.addListener(this::createAttributes);

        NeoForge.EVENT_BUS.addListener(this::addReloadListeners);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void createAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BEYBLADE.get(), Beyblade.createAttributes().build());
    }

    private void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new BeypartsReloadListener());
    }

    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            List<CompoundTag> serializedParts = new ArrayList<>();
            BeypartsReloadListener.BEYPARTS.forEach(((resourceLocation, beypart) -> {
                BeypartsReloadListener.addSerializedPart(beypart, serializedParts);
            }));
            PacketDistributor.sendToPlayer(player, new SyncBeyparts(serializedParts));
//            PacketDistributor.sendToPlayer(player, new SyncBeyparts(BeypartsReloadListener.BEYPARTS.values().stream().toList()));
        }
    }

    private void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MOD_ID);

        registrar.commonToClient(SyncBeyparts.TYPE, SyncBeyparts.STREAM_CODEC, SyncBeyparts::handle);
    }

}
