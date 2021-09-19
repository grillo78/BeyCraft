package com.grillo78.beycraft;

import com.grillo78.beycraft.capabilities.*;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.tab.BeyCraftDisksTab;
import com.grillo78.beycraft.tab.BeyCraftDriversTab;
import com.grillo78.beycraft.tab.BeyCraftLayersTab;
import com.grillo78.beycraft.tab.BeyCraftTab;
import com.grillo78.beycraft.util.ConfigManager;
import friedrichlp.renderlib.RenderLibSettings;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MOD_ID)
public class BeyCraft {

    // Directly reference a log4j logger.
    public static final Logger logger = LogManager.getLogger();

    public static long TIME_STAMP;

    public static final ItemGroup BEYCRAFTLAYERS = new BeyCraftLayersTab("Layers");
    public static final ItemGroup BEYCRAFTDISKS = new BeyCraftDisksTab("Disks");
    public static final ItemGroup BEYCRAFTDRIVERS = new BeyCraftDriversTab("Drivers");
    public static final ItemGroup BEYCRAFTTAB = new BeyCraftTab("Beycraft");


    public BeyCraft() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Load the config of the mod
        ConfigManager.load();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->{
            RenderLibSettings.Caching.CACHE_LOCATION = "beycraft_cached_models";
            RenderLibSettings.Caching.CACHE_VERSION = "1";
            try {
                URL url = new URL("http://www.google.com");
                URLConnection connection = url.openConnection();
                connection.connect();
                Reference.HAS_INTERNET = true;
            } catch (MalformedURLException e) {
                System.out.println("Internet is not connected");
            } catch (IOException e) {
                System.out.println("Internet is not connected");
            }
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        CapabilityManager.INSTANCE.register(IBladerLevel.class, new BladerLevelStorage(), new BladerLevelFactory());
        CapabilityManager.INSTANCE.register(ICurrency.class, new CurrencyStorage(), new CurrencyFactory());
    }

}
