package grillo78.beycraft;

import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.items.ModItems;
import grillo78.beycraft.items.components.ModDataComponents;
import grillo78.beycraft.network.RegisterModel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Beycraft.MOD_ID)
public class Beycraft {

    public static final String MOD_ID = "beycraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Beycraft(IEventBus modEventBus, ModContainer modContainer) {
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::registerPackets);

        NeoForge.EVENT_BUS.addListener(this::addReloadListeners);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    private void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new BeypartsReloadListener());
    }

    private void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MOD_ID);

        registrar.commonToClient(RegisterModel.TYPE, RegisterModel.STREAM_CODEC, RegisterModel::handle);
    }

}
