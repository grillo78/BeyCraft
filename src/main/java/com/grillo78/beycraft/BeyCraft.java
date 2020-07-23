package com.grillo78.beycraft;

import com.grillo78.beycraft.capabilities.BladerLevelStorage;
import com.grillo78.beycraft.capabilities.Factory;
import com.grillo78.beycraft.capabilities.IBladerLevel;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.tab.BeyCraftDisksTab;
import com.grillo78.beycraft.tab.BeyCraftDriversTab;
import com.grillo78.beycraft.tab.BeyCraftLayersTab;
import com.grillo78.beycraft.tab.BeyCraftTab;
import com.grillo78.beycraft.util.ConfigManager;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

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
        // Load the config of the mod
        ConfigManager.load();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        CapabilityManager.INSTANCE.register(IBladerLevel.class, new BladerLevelStorage(), new Factory());
    }

}
