package ga.beycraft;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.RenderLibSettings;
import friedrichlp.renderlib.math.Vector3;
import friedrichlp.renderlib.tracking.RenderManager;
import ga.beycraft.client.block.BeycreatorRenderer;
import ga.beycraft.client.entity.BeybladeRenderer;
import ga.beycraft.client.particle.ResonanceParticle;
import ga.beycraft.client.particle.SparkleParticle;
import ga.beycraft.client.screen.*;
import ga.beycraft.client.util.BeyPartModel;
import ga.beycraft.client.util.KeyBinds;
import ga.beycraft.common.block.ModBlocks;
import ga.beycraft.common.block_entity.ModTileEntities;
import ga.beycraft.common.capability.entity.*;
import ga.beycraft.common.capability.item.beylogger.Beylogger;
import ga.beycraft.common.capability.item.beylogger.BeyloggerStorage;
import ga.beycraft.common.capability.item.beylogger.IBeylogger;
import ga.beycraft.common.container.ModContainers;
import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.entity.ModEntities;
import ga.beycraft.common.item.BeyPartItem;
import ga.beycraft.common.item.ModItems;
import ga.beycraft.common.launch.LaunchTypes;
import ga.beycraft.common.particle.ModParticles;
import ga.beycraft.common.stats.CustomStats;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageGetExperience;
import ga.beycraft.utils.ClientUtils;
import ga.beycraft.utils.CommonUtils;
import ga.beycraft.utils.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.BuiltInModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import vazkii.patchouli.api.BookContentsReloadEvent;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Mod(Beycraft.MOD_ID)
public class Beycraft {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "beycraft";
    public static boolean HAS_INTERNET = true;
    private boolean firstScreenMenuOpen = true;

    public Beycraft() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        FMLJavaModLoadingContext.get().getModEventBus().register(new SpecialEvents());
        MinecraftForge.EVENT_BUS.register(new SpecialRuntimeEvents());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::onBookReload);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityEnterWorld);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            try {
                URL url = new URL("http://www.google.com");
                URLConnection connection = url.openConnection();
                connection.connect();
            } catch (MalformedURLException e) {
                HAS_INTERNET = false;
                System.out.println("Internet is not connected");
            } catch (IOException e) {
                HAS_INTERNET = false;
                System.out.println("Internet is not connected");
            }
            try {
                File folder = new File("beycraft_cached_models");
                if (folder.exists())
                    FileUtils.deleteDirectory(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RenderLibSettings.Caching.CACHE_LOCATION = "beycraft_cached_models";
            RenderLibSettings.Caching.CACHE_VERSION = "1";
            RenderLibSettings.General.MODEL_UNLOAD_DELAY_MS = Integer.MAX_VALUE;
//            RenderLibSettings.Caching.CHECK_CACHE = false;
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onParticleFactoriesRegistry);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelBake);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerModel);
            MinecraftForge.EVENT_BUS.addListener(this::onRenderWorld);
            MinecraftForge.EVENT_BUS.addListener(this::renderHand);
            MinecraftForge.EVENT_BUS.addListener(this::onKeyPressed);
            MinecraftForge.EVENT_BUS.addListener(this::onScreenOpen);
            MinecraftForge.EVENT_BUS.addListener(this::onScreenRender);
        });
        if (HAS_INTERNET) {
            try {
                downloadDefaultPack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModContainers.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModTileEntities.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModParticles.PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LaunchTypes.LAUNCH_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void downloadDefaultPack() throws IOException {
        if (Config.PRE_SETUP.downloadDefaultPack) {
            BufferedInputStream in = new BufferedInputStream(new URL("https://beycraft.ga/Starter%20Pack.zip").openStream());
            File itemsFolder = new File("BeyParts");
            if (!itemsFolder.exists()) {
                itemsFolder.mkdir();
            }
            File starterPackFile = new File("BeyParts/Starter Pack temp.zip");
            FileOutputStream fileOutputStream = new FileOutputStream(starterPackFile);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            fileOutputStream.close();
            CommonUtils.ZipUtils.unzip(starterPackFile, itemsFolder);
            starterPackFile.delete();
        }
    }

    private void onBookReload(BookContentsReloadEvent event) {
        Book book = BookRegistry.INSTANCE.books.get(event.book);
        for (BeyPartItem item : BeyPartItem.getParts()) {
            BookEntry entry = ClientBookRegistry.INSTANCE.gson.fromJson(item.getEntryJson(), BookEntry.class);
            entry.setBook(book);
            BookCategory category = entry.getCategory();
            if (category != null) {
                category.addEntry(entry);
            }
            entry.setId(item.getRegistryName());
            entry.build();
            book.contents.entries.put(entry.getId(), entry);
        }
    }

    private void onEntityEnterWorld(final PlayerEvent.PlayerLoggedInEvent event) {
        StringTextComponent prefix = new StringTextComponent("[BeyCraft] -> Join to my Discord server: ");
        StringTextComponent url = new StringTextComponent("https://discord.gg/2PpbtFr");
        prefix.withStyle(TextFormatting.GOLD);
        url.withStyle(TextFormatting.GOLD);
        event.getPlayer().sendMessage(prefix, Util.NIL_UUID);
        event.getPlayer().sendMessage(url, Util.NIL_UUID);
        event.getPlayer().getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(blader -> {
            blader.syncToAll();
        });
        PacketHandler.INSTANCE.sendTo(new MessageGetExperience(),
                ((ServerPlayerEntity) event.getPlayer()).connection.getConnection(),
                NetworkDirection.PLAY_TO_CLIENT);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        CapabilityManager.INSTANCE.register(IBeylogger.class, new BeyloggerStorage(), Beylogger::new);
        CapabilityManager.INSTANCE.register(IBlader.class, new BladerStorage(), Blader::new);
        CustomStats.init();
    }

    @OnlyIn(Dist.CLIENT)
    private void onParticleFactoriesRegistry(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.SPARKLE, SparkleParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticles.RESONANCE, ResonanceParticle.Factory::new);
    }

    @OnlyIn(Dist.CLIENT)
    private void onScreenOpen(final GuiOpenEvent event) {
        if (event.getGui() instanceof MainMenuScreen && firstScreenMenuOpen) {
            firstScreenMenuOpen = false;
            try {
                ClientUtils.setDiscordRPC();
            } catch (Exception e) {
                LOGGER.error("Error during Discord RPC start");
            }
            if (!HAS_INTERNET) {
                event.setCanceled(true);
                Minecraft.getInstance()
                        .setScreen(new NoInternetConnectionScreen(new StringTextComponent("")));
            } else try {
                ClientUtils.RankingUtils.checkLogin(event);
            } catch (Exception e) {
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void onScreenRender(RenderGameOverlayEvent.Post event) {
        if (!Minecraft.getInstance().options.renderDebug) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(cap -> {
                    Level h = cap.getBladerLevel();
                    String s = String.valueOf(h.getLevel());
                    float i1 = (75 - Minecraft.getInstance().font.width(s)) / 2f;
                    int j1 = 16;
                    Minecraft.getInstance().font.drawShadow(event.getMatrixStack(), s, i1,
                            (float) j1, 8453920);
                    s = "Blader Level:";

                    i1 = 5;
                    j1 = 5;
                    Minecraft.getInstance().font.drawShadow(event.getMatrixStack(), s, i1,
                            (float) j1, 8453920);
                    Minecraft.getInstance().getEntityRenderDispatcher().textureManager
                            .bind(AbstractGui.GUI_ICONS_LOCATION);
                    float i = (h.getExpForNextLevel() - h.getExperience()) / (Level.calcExpForNextLevel(h.getLevel()+1)-Level.calcExpForNextLevel(h.getLevel()));
                    AbstractGui.blit(event.getMatrixStack(), 1, 27, 0, 74, 75, 5, 105, 256);
                    AbstractGui.blit(event.getMatrixStack(), 1, 27, 0, 79, (int) (75 - 75 * i), 5, 105, 256);
                });
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void onRenderWorld(RenderWorldLastEvent event) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        RenderManager.setCameraPos(new Vector3((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z));
        RenderLibSettings.Rendering.CAMERA_HEIGHT_OFFSET = (float) (cameraPos.y - Minecraft.getInstance().player.getPosition(event.getPartialTicks()).y);
        RenderManager.setRenderDistance(Minecraft.getInstance().options.renderDistance * 16);
        try {
            BeyPartModel.WORLD_MODELS.forEach(h -> h.render());
            RenderManager.update();
        } catch (NullPointerException e) {
        }
        for (Runnable runnable : BeybladeRenderer.RUNNABLES) {
            runnable.run();
        }
        for (Runnable runnable : BeycreatorRenderer.RUNNABLES) {
            runnable.run();
        }
        BeyPartModel.WORLD_MODELS.clear();
        BeybladeRenderer.RUNNABLES.clear();
        BeycreatorRenderer.RUNNABLES.clear();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    @OnlyIn(Dist.CLIENT)
    private void renderHand(RenderHandEvent event) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        BeyPartModel.HAND_MODELS.forEach(h -> h.render());
        BeyPartModel.HAND_MODELS.clear();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    @OnlyIn(Dist.CLIENT)
    private void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (KeyBinds.LAUNCH_SCREEN.isDown() && event.getAction() == GLFW.GLFW_PRESS) {
            Minecraft.getInstance().setScreen(new LaunchScreen(new TranslationTextComponent("screen.launch_screen.name")));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerModel(final ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "launchers/dual_launcher/launcher_body"));
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "launchers/dual_launcher/grab_part"));
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "launchers/right_launcher/launcher_body"));
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "launchers/right_launcher/grab_part"));
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "launchers/left_launcher/launcher_body"));
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "launchers/left_launcher/grab_part"));
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "beyloggers/beylogger_plus"));
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "beyloggers/beylogger"));
    }

    @OnlyIn(Dist.CLIENT)
    private void onModelBake(ModelBakeEvent event) {
        for (BeyPartItem item : BeyPartItem.getParts()) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
            event.getModelRegistry().put(modelResourceLocation, new BuiltInModel(ItemCameraTransforms.NO_TRANSFORMS, ItemOverrideList.EMPTY.EMPTY, MissingTextureSprite.newInstance(new AtlasTexture(PlayerContainer.BLOCK_ATLAS), 0, 10, 10, 0, 0), true));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.BEYCREATOR, BeycreatorRenderer::new);
        ClientRegistry.registerKeyBinding(KeyBinds.LAUNCH_SCREEN);
        RenderTypeLookup.setRenderLayer(ModBlocks.STADIUM, RenderType.cutoutMipped());
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BEYBLADE, BeybladeRenderer::new);
        ScreenManager.register(ModContainers.LAYER, LayerScreen::new);
        ScreenManager.register(ModContainers.RIGHT_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.LEFT_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.DUAL_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.DISC_FRAME, DiscFrameScreen::new);
    }

    public static class SpecialEvents {

        @SubscribeEvent
        public void registerEntityType(final RegistryEvent.Register<EntityType<?>> event) {

            GlobalEntityTypeAttributes.put(ModEntities.BEYBLADE,
                    BeybladeEntity.registerMonsterAttributes().build());

        }
    }

    public static class SpecialRuntimeEvents {

        @SubscribeEvent
        public void playerCapabilitiesInjection(final AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof PlayerEntity) {
                event.addCapability(new ResourceLocation(MOD_ID, "blader"), new BladerCapabilityProvider((PlayerEntity) event.getObject()));
            }
        }
    }
}
