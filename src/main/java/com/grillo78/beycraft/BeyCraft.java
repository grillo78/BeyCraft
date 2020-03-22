package com.grillo78.beycraft;

import com.grillo78.beycraft.gui.*;
import com.grillo78.beycraft.inventory.*;
import com.grillo78.beycraft.items.ItemBladerBelt;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageOpenBelt;
import com.grillo78.beycraft.particles.SparkleParticle;
import com.grillo78.beycraft.tileentity.RenderExpository;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.resources.*;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.grillo78.beycraft.entity.BeyEntityRenderFactory;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.tab.BeyCraftDisksTab;
import com.grillo78.beycraft.tab.BeyCraftDriversTab;
import com.grillo78.beycraft.tab.BeyCraftLayersTab;
import com.grillo78.beycraft.tab.BeyCraftTab;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;
import com.grillo78.beycraft.util.ItemCreator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MODID)
public class BeyCraft {

    // Directly reference a log4j logger.
    public static final Logger logger = LogManager.getLogger();

    public static final ItemGroup BEYCRAFTLAYERS = new BeyCraftLayersTab("Layers");
    public static final ItemGroup BEYCRAFTDISKS = new BeyCraftDisksTab("Disks");
    public static final ItemGroup BEYCRAFTDRIVERS = new BeyCraftDriversTab("Drivers");
    public static final ItemGroup BEYCRAFTTAB = new BeyCraftTab("Beycraft");

    @ObjectHolder(Reference.MODID + ":expositorytileentity")
    public static TileEntityType<ExpositoryTileEntity> EXPOSITORYTILEENTITYTYPE = (TileEntityType<ExpositoryTileEntity>) TileEntityType.Builder.create(ExpositoryTileEntity::new, BeyRegistry.EXPOSITORY).build(null)
            .setRegistryName(new ResourceLocation(Reference.MODID, "expositorytileentity"));

    public BeyCraft() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class, this::registerEntityType);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class,
                this::registerTileEntityType);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerEntityType(RegistryEvent.Register<EntityType<?>> event) {
        EntityType<?> type = EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC)
                .size(0.19F, 0.25F).build(Reference.MODID + ":bey");
        type.setRegistryName(Reference.MODID, "bey");
        event.getRegistry().register(type);
    }

    private void registerTileEntityType(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(EXPOSITORYTILEENTITYTYPE);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        RenderTypeLookup.setRenderLayer(BeyRegistry.STADIUM, RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(BeyRegistry.EXPOSITORY, RenderType.getCutoutMipped());
        ClientRegistry.registerKeyBinding(BeyRegistry.beltKey);
        ScreenManager.registerFactory(BeyRegistry.LAUNCHER_CONTAINER, LauncherGUI::new);
        ScreenManager.registerFactory(BeyRegistry.DISK_FRAME_CONTAINER, DiskFrameGUI::new);
        try {
            Class.forName("com.lazy.baubles.Baubles");
            ScreenManager.registerFactory(BeyRegistry.BELT_CONTAINER, BeltGUI::new);
        } catch (Exception e) {
        }
        ScreenManager.registerFactory(BeyRegistry.BEY_CONTAINER, BeyGUI::new);
        ScreenManager.registerFactory(BeyRegistry.HANDLE_CONTAINER, HandleGUI::new);
        for (Item item : BeyRegistry.ITEMSLAYER) {
            ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "layers/" + item.getTranslationKey().replace("item.beycraft.", "")));
        }
        RenderingRegistry.registerEntityRenderingHandler(BeyRegistry.BEY_ENTITY_TYPE.get(),
                new BeyEntityRenderFactory());
        ClientRegistry.bindTileEntityRenderer(EXPOSITORYTILEENTITYTYPE, RenderExpository::new);
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

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventsHandler {

        @SubscribeEvent
        public static void onKeyPressed(InputEvent.KeyInputEvent event) {
            if (Minecraft.getInstance().player == null)
                return;
            if (BeyRegistry.beltKey.isPressed()) {
                try {
                    Class.forName("com.lazy.baubles.Baubles");
                    PacketHandler.instance.sendToServer(new MessageOpenBelt());
                } catch (Exception e) {

                }
            }
        }


        @SubscribeEvent
        public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof PlayerEntity) {

            }
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @ObjectHolder(Reference.MODID + ":sparkle")
        public static BasicParticleType SPARKLE;

        @SubscribeEvent
        public static void onParticleTypeRegistry(final RegistryEvent.Register<ParticleType<?>> event) {
            event.getRegistry().register(new BasicParticleType(false).setRegistryName("sparkle"));
        }

        @SubscribeEvent
        public static void onSoundRegistry(final RegistryEvent.Register<SoundEvent> event) {
            BeyRegistry.HITSOUND.setRegistryName("bey.hit");
            event.getRegistry().register(BeyRegistry.HITSOUND);
        }

        @SubscribeEvent
        public static void onParticleFactorieRegistry(final ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particles.registerFactory(SPARKLE, SparkleParticle.Factory::new);
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new BeyDiskFrameContainer(windowId, new ItemStack(BeyRegistry.REDLAUNCHER), inv, inv.player, Hand.MAIN_HAND);
            }).setRegistryName("diskframe"));
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new LauncherContainer(BeyRegistry.LAUNCHER_CONTAINER, windowId, new ItemStack(BeyRegistry.REDLAUNCHER), inv, inv.player,
                        1, Hand.MAIN_HAND);
            }).setRegistryName("launcher"));
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new BeyContainer(BeyRegistry.BEY_CONTAINER, windowId, new ItemStack(BeyRegistry.ITEMSLAYER.get(0)), inv, inv.player, Hand.MAIN_HAND);
            }).setRegistryName("bey"));
            try {
                Class.forName("com.lazy.baubles.Baubles");
                event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                    return new BeltContainer(BeyRegistry.BELT_CONTAINER, windowId, new ItemStack(BeyRegistry.ITEMS.get("belt")), inv);
                }).setRegistryName("belt"));
            } catch (Exception e) {
            }
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new HandleContainer(BeyRegistry.HANDLE_CONTAINER, windowId, new ItemStack(BeyRegistry.REDLAUNCHER), inv, inv.player, Hand.MAIN_HAND);
            }).setRegistryName("handle"));
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            // register a new block here
            for (Block block : BeyRegistry.BLOCKS) {
                event.getRegistry().register(block);
            }
        }

        @SubscribeEvent
        public static void registerItem(final RegistryEvent.Register<Item> event) {
            File itemsFolder = new File(Minecraft.getInstance().gameDir, "BeyParts");
            File[] zipFiles = itemsFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".zip");
                }
            });
            try{
                for (File file : zipFiles) {
                    injectResources(file);
                }
            }catch (Exception e){
                
            }
            ItemCreator.getItemsFromFolder();
            BeyRegistry.ITEMS.forEach((name,item) -> event.getRegistry().register(item));
            try {
                Class.forName("com.lazy.baubles.Baubles");
                event.getRegistry().register(new ItemBladerBelt("belt"));
            } catch (Exception e) {
            }
            for (Item item : BeyRegistry.ITEMSLAYER) {
                event.getRegistry().register(item);
            }
            for (Item item : BeyRegistry.ITEMSFRAMELIST) {
                event.getRegistry().register(item);
            }
            for (Item item : BeyRegistry.ITEMSDISKLIST) {
                event.getRegistry().register(item);
            }
            for (Item item : BeyRegistry.ITEMSDRIVER) {
                event.getRegistry().register(item);
            }
        }



        public static void injectResources(File file){
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;

            try {
                final String id = file.getName().replace(".zip","").replace(" ","_")+"_addon_resources";
                final ITextComponent name = new StringTextComponent(file.getName().replace(".zip","")+" Resources");
                final ITextComponent description = new StringTextComponent("Beycraft addon resources.");

                final IResourcePack pack = new FilePack(file){
                    String prefix = "assets/beycraft";

                    @Override
                    protected InputStream getInputStream(String resourcePath) throws IOException {
                        if ("pack.mcmeta".equals(resourcePath)) return new ByteArrayInputStream(("{\"pack\":{\"description\": \"dummy\",\"pack_format\": 4}}").getBytes(StandardCharsets.UTF_8));
                        if (!resourcePath.startsWith(prefix)) throw new FileNotFoundException(resourcePath);

                        return super.getInputStream(resourcePath);
                    }

                    @Override
                    public boolean resourceExists(String resourcePath) {
                        if ("pack.mcmeta".equals(resourcePath)) return true;
                        if (!resourcePath.startsWith(prefix)) return false;

                        return super.resourceExists(resourcePath);
                    }
                };

                Minecraft.getInstance().getResourcePackList().addPackFinder(new IPackFinder() {
                    @Override
                    public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
                        nameToPackMap.put(id, (T) new ClientResourcePackInfo(id, true, () ->
                                pack, name, description, PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.TOP, false, null, false
                        ));
                    }
                });
            } catch (Exception e){

            }
        }

//		@SubscribeEvent
//		public static void playerJoined(final event event) {
//			PlayerEntity player = event.getPlayer();
//			ITextComponent prefix = TextComponentUtils.toTextComponent(new Message() {
//
//				@Override
//				public String getString() {
//					return "[BeyCraft] -> Join to my Discord server: ";
//				}
//			});
//			ITextComponent url = TextComponentUtils.toTextComponent(new Message() {
//
//				@Override
//				public String getString() {
//					return "https://discord.gg/2PpbtFr";
//				}
//			});
//			Style sPrefix = new Style();
//			sPrefix.setColor(TextFormatting.GOLD);
//			Style sUrl = new Style();
//			sUrl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/2PpbtFr"))
//					.setColor(TextFormatting.GOLD);
//			prefix.setStyle(sPrefix);
//			url.setStyle(sUrl);
//			player.sendMessage(prefix);
//			player.sendMessage(url);
////			BeyCraft.INSTANCE.sendTo(
////					new BladerLevelMessage(
////							(int) event.player.getCapability(Provider.BLADERLEVEL_CAP, null).getBladerLevel()),
////					(PlayerEntityMP) event.player);
//		}

        @SubscribeEvent
        public static void editHud(RenderGameOverlayEvent.Post event) {
            if (!Minecraft.getInstance().gameSettings.showDebugInfo) {
                if (event.getType() == ElementType.ALL) {
                    Minecraft.getInstance().getRenderManager().textureManager
                            .bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/bladerlevel.png"));
                }
            }
        }

    }
}
