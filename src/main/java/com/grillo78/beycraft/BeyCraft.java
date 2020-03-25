package com.grillo78.beycraft;

import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.tab.BeyCraftDisksTab;
import com.grillo78.beycraft.tab.BeyCraftDriversTab;
import com.grillo78.beycraft.tab.BeyCraftLayersTab;
import com.grillo78.beycraft.tab.BeyCraftTab;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MODID)
public class BeyCraft {

    // Directly reference a log4j logger.
    public static final Logger logger = LogManager.getLogger();

    public static final ItemGroup BEYCRAFTLAYERS = new BeyCraftLayersTab("Layers");
    public static final ItemGroup BEYCRAFTDISKS = new BeyCraftDisksTab("Disks");
    public static final ItemGroup BEYCRAFTDRIVERS = new BeyCraftDriversTab("Drivers");
    public static final ItemGroup BEYCRAFTTAB = new BeyCraftTab("Beycraft");


    public BeyCraft() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }
}
