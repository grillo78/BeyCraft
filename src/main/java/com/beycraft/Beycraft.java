package com.beycraft;

import com.beycraft.client.block.BattleInformerRenderer;
import com.beycraft.client.block.BeycreatorRenderer;
import com.beycraft.client.block.ExpositoryRenderer;
import com.beycraft.client.entity.BeybladeRenderer;
import com.beycraft.client.particle.ResonanceParticle;
import com.beycraft.client.particle.SparkleParticle;
import com.beycraft.client.screen.*;
import com.beycraft.client.util.BeyPartModel;
import com.beycraft.client.util.KeyBinds;
import com.beycraft.common.block.ModBlocks;
import com.beycraft.common.block_entity.ModTileEntities;
import com.beycraft.common.capability.entity.Blader;
import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import com.beycraft.common.capability.entity.BladerStorage;
import com.beycraft.common.capability.item.beylogger.Beylogger;
import com.beycraft.common.capability.item.beylogger.BeyloggerStorage;
import com.beycraft.common.capability.item.beylogger.IBeylogger;
import com.beycraft.common.commands.BeyCoinsCommand;
import com.beycraft.common.commands.GetBeyCoinsCommand;
import com.beycraft.common.commands.LevelCommand;
import com.beycraft.common.container.ModContainers;
import com.beycraft.common.entity.BeybladeEntity;
import com.beycraft.common.entity.ModEntities;
import com.beycraft.common.item.BeyPartItem;
import com.beycraft.common.item.LayerItem;
import com.beycraft.common.item.ModItems;
import com.beycraft.common.launch.LaunchTypes;
import com.beycraft.common.particle.ModParticles;
import com.beycraft.common.ranking.Level;
import com.beycraft.common.sound.ModSounds;
import com.beycraft.common.stats.CustomStats;
import com.beycraft.network.PacketHandler;
import com.beycraft.network.message.MessageActivateLaunch;
import com.beycraft.network.message.MessageActivateResonance;
import com.beycraft.network.message.MessageHandSpinning;
import com.beycraft.network.message.MessageOpenBeltContainer;
import com.beycraft.utils.ClientUtils;
import com.beycraft.utils.Config;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.RenderLibSettings;
import friedrichlp.renderlib.math.Vector3;
import friedrichlp.renderlib.tracking.RenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
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
import net.minecraftforge.event.RegisterCommandsEvent;
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

import java.io.File;
import java.io.IOException;

@Mod(Beycraft.MOD_ID)
public class Beycraft {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "beycraft";
    private boolean firstScreenMenuOpen = true;

    public Beycraft() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        FMLJavaModLoadingContext.get().getModEventBus().register(new SpecialEvents());
        MinecraftForge.EVENT_BUS.register(new SpecialRuntimeEvents());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::onBookReload);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityEnterWorld);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerClone);
        MinecraftForge.EVENT_BUS.addListener(this::onCommandRegistry);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
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
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onParticleFactoriesRegistry);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelBake);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerModel);
            MinecraftForge.EVENT_BUS.addListener(this::onRenderWorld);
            MinecraftForge.EVENT_BUS.addListener(this::renderHand);
            MinecraftForge.EVENT_BUS.addListener(this::onKeyPressed);
            MinecraftForge.EVENT_BUS.addListener(this::onMouseScroll);
            MinecraftForge.EVENT_BUS.addListener(this::onScreenOpen);
            MinecraftForge.EVENT_BUS.addListener(this::onScreenRender);
            MinecraftForge.EVENT_BUS.addListener(this::onInputUpdate);
            MinecraftForge.EVENT_BUS.addListener(this::onMouseCLick);
        });

        ModSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModContainers.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModTileEntities.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModParticles.PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LaunchTypes.LAUNCH_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void onCommandRegistry(final RegisterCommandsEvent event) {
        BeyCoinsCommand.register(event.getDispatcher());
        GetBeyCoinsCommand.register(event.getDispatcher());
        LevelCommand.register(event.getDispatcher());
    }

    private void onBookReload(BookContentsReloadEvent event) {
        Book book = BookRegistry.INSTANCE.books.get(event.book);
        if (event.book.getNamespace().equals(MOD_ID))
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
    }

    private void onPlayerClone(final PlayerEvent.Clone event) {
        event.getOriginal().getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h ->
                event.getPlayer().getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(c -> c.readNBT(h.writeNBT()))
        );
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        CapabilityManager.INSTANCE.register(IBeylogger.class, new BeyloggerStorage(), Beylogger::new);
        CapabilityManager.INSTANCE.register(Blader.class, new BladerStorage(), Blader::new);
        CustomStats.init();
    }

    @OnlyIn(Dist.CLIENT)
    private void onParticleFactoriesRegistry(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.SPARKLE, SparkleParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticles.RESONANCE, ResonanceParticle.Factory::new);
    }

    @OnlyIn(Dist.CLIENT)
    public void onInputUpdate(InputUpdateEvent e) {
        Blader blader = e.getPlayer().getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null);
        if (blader != null && blader.isLaunching()) {
            e.getMovementInput().right = false;
            e.getMovementInput().left = false;
            e.getMovementInput().up = false;
            e.getMovementInput().down = false;
            e.getMovementInput().shiftKeyDown = false;
            e.getMovementInput().jumping = false;
            e.getMovementInput().forwardImpulse = 0;
            e.getMovementInput().leftImpulse = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void onMouseCLick(InputEvent.ClickInputEvent e) {
        if (e.isAttack() && Minecraft.getInstance().player.getMainHandItem().getItem() instanceof LayerItem && ((LayerItem) Minecraft.getInstance().player.getMainHandItem().getItem()).isBeyAssembled(Minecraft.getInstance().player.getMainHandItem())) {
            PacketHandler.INSTANCE.sendToServer(new MessageHandSpinning());
            e.setCanceled(true);
        }
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
        }
        if (event.getGui() instanceof ContainerScreen) {
            Blader blader = Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null);
            if (blader != null && blader.isLaunching())
                event.setCanceled(true);
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
                    float i = (h.getExpForNextLevel() - h.getExperience()) / (Level.calcExpForNextLevel(h.getLevel() + 1) - Level.calcExpForNextLevel(h.getLevel()));
                    AbstractGui.blit(event.getMatrixStack(), 1, 27, 0, 74, 75, 5, 105, 256);
                    AbstractGui.blit(event.getMatrixStack(), 1, 27, 0, 79, (int) (-75*i), 5, 105, 256);
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
    private void onMouseScroll(InputEvent.MouseScrollEvent event) {
        Blader blader = Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null);
        if (blader != null && blader.isLaunching())
            event.setCanceled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (KeyBinds.LAUNCH_SCREEN.isDown() && event.getAction() == GLFW.GLFW_PRESS) {
            Minecraft.getInstance().setScreen(new LaunchScreen(new TranslationTextComponent("screen.launch_screen.name")));
        }
        if (KeyBinds.ACTIVATE_LAUNCH.isDown() && event.getAction() == GLFW.GLFW_PRESS) {
            PacketHandler.INSTANCE.sendToServer(new MessageActivateLaunch());
        }
        if (KeyBinds.ACTIVATE_RESONANCE.isDown() && event.getAction() == GLFW.GLFW_PRESS) {
            PacketHandler.INSTANCE.sendToServer(new MessageActivateResonance());
        }
        if (KeyBinds.BELT.isDown() && event.getAction() == GLFW.GLFW_PRESS) {
            PacketHandler.INSTANCE.sendToServer(new MessageOpenBeltContainer());
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
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.BATTLE_INFORMER, BattleInformerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.EXPOSITORYTILEENTITYTYPE, ExpositoryRenderer::new);
        KeyBinds.registerKeys();
        RenderTypeLookup.setRenderLayer(ModBlocks.STADIUM, RenderType.cutoutMipped());
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BEYBLADE, BeybladeRenderer::new);
        ScreenManager.register(ModContainers.LAYER, LayerScreen::new);
        ScreenManager.register(ModContainers.CLEAR_WHEEL, LayerScreen::new);
        ScreenManager.register(ModContainers.RIGHT_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.LEFT_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.DUAL_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.DISC_FRAME, DiscFrameScreen::new);
        ScreenManager.register(ModContainers.BELT_CONTAINER, BeltScreen::new);
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
