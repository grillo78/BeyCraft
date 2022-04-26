package ga.beycraft;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.RenderLibSettings;
import friedrichlp.renderlib.math.Vector3;
import friedrichlp.renderlib.tracking.RenderManager;
import ga.beycraft.client.entity.BeybladeRenderer;
import ga.beycraft.client.screen.DiscFrameScreen;
import ga.beycraft.client.screen.LauncherScreen;
import ga.beycraft.client.screen.LayerScreen;
import ga.beycraft.client.util.BeyPartModel;
import ga.beycraft.common.block.ModBlocks;
import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.entity.ModEntities;
import ga.beycraft.common.capability.item.beylogger.Beylogger;
import ga.beycraft.common.capability.item.beylogger.BeyloggerStorage;
import ga.beycraft.common.capability.item.beylogger.IBeylogger;
import ga.beycraft.common.container.ModContainers;
import ga.beycraft.common.item.*;
import ga.beycraft.utils.CommonUtils;
import ga.beycraft.utils.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.BuiltInModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.heroesunited.heroesunited.client.events.HUSetRotationAnglesEvent;

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

    public Beycraft() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        FMLJavaModLoadingContext.get().getModEventBus().register(new SpecialEvents());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
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
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelBake);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerModel);
            MinecraftForge.EVENT_BUS.addListener(this::onRenderWorld);
            MinecraftForge.EVENT_BUS.addListener(this::renderHand);
            MinecraftForge.EVENT_BUS.addListener(this::setPlayerRotations);
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
    }

    private void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IBeylogger.class, new BeyloggerStorage(), Beylogger::new);
    }

    private void downloadDefaultPack() throws IOException {
        if (Config.COMMON.downloadDefaultPack.get()) {
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
        BeyPartModel.WORLD_MODELS.clear();
        BeybladeRenderer.RUNNABLES.clear();
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
        RenderTypeLookup.setRenderLayer(ModBlocks.STADIUM, RenderType.cutoutMipped());
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BEYBLADE, BeybladeRenderer::new);
        ScreenManager.register(ModContainers.LAYER, LayerScreen::new);
        ScreenManager.register(ModContainers.RIGHT_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.LEFT_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.DUAL_LAUNCHER, LauncherScreen::new);
        ScreenManager.register(ModContainers.DISC_FRAME, DiscFrameScreen::new);
    }

    @OnlyIn(Dist.CLIENT)
    private void setPlayerRotations(final HUSetRotationAnglesEvent event) {
        PlayerEntity player = event.getPlayer();
        PlayerModel model = event.getPlayerModel();
        if (player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof LauncherItem
                || player.getItemInHand(Hand.OFF_HAND).getItem() instanceof LauncherItem) {
            model.leftArm.xRot = (float) Math.toRadians(player.xRot - 90);
            model.rightArm.xRot = (float) Math.toRadians(player.xRot - 90);

            model.leftSleeve.xRot = (float) Math.toRadians(player.xRot - 90);
            model.rightSleeve.xRot = (float) Math.toRadians(player.xRot - 90);

            model.leftArm.yRot = (float) Math.toRadians(20);
            model.rightArm.yRot = (float) Math.toRadians(-20);
            model.rightArm.zRot = (float) Math
                    .toRadians(25 * (1 - ((90 - (-player.xRot)) / 90)));
            model.leftArm.zRot = (float) Math.toRadians(25 * (1 - ((90 - player.xRot) / 90)));

            model.leftSleeve.yRot = (float) Math.toRadians(20);
            model.rightSleeve.yRot = (float) Math.toRadians(-20);
            model.rightSleeve.zRot = (float) Math
                    .toRadians(25 * (1 - ((90 - (-player.xRot)) / 90)));
            model.leftSleeve.zRot = (float) Math
                    .toRadians(25 * (1 - ((90 - player.xRot) / 90)));
        }

        if (player.getMainHandItem().getItem() instanceof LauncherItem) {
            player.getMainHandItem().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap->{
                if (cap.getStackInSlot(1).getItem() instanceof HandleItem){
                    if(player.getMainArm() ==HandSide.RIGHT){
                        model.rightArm.yRot = (float) Math.toRadians(0);
                        model.rightArm.zRot = (float) Math.toRadians(0);
                        model.rightSleeve.yRot = (float) Math.toRadians(0);
                        model.rightSleeve.zRot = (float) Math.toRadians(0);

                    } else {
                        model.leftArm.yRot = (float) Math.toRadians(0);
                        model.leftArm.zRot = (float) Math.toRadians(0);
                        model.leftSleeve.yRot = (float) Math.toRadians(0);
                        model.leftSleeve.zRot = (float) Math.toRadians(0);
                    }
                }
                if (player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())) {
                    if (player.getMainArm() == HandSide.RIGHT) {
                        model.leftArm.yRot = (float) Math.toRadians(-25);
                        model.leftArm.zRot = (float) Math
                                .toRadians(-25 * (1 - ((90 - player.xRot) / 90)));
                        model.leftSleeve.yRot = (float) Math.toRadians(-25);
                        model.leftSleeve.zRot = (float) Math
                                .toRadians(-25 * (1 - ((90 - player.xRot) / 90)));
                    } else {
                        model.rightArm.yRot = (float) Math.toRadians(25);
                        model.rightArm.zRot = (float) Math
                                .toRadians(-25 * (1 - ((90 - (-player.xRot)) / 90)));
                        model.rightSleeve.yRot = (float) Math.toRadians(25);
                        model.rightSleeve.zRot = (float) Math
                                .toRadians(-25 * (1 - ((90 - (-player.xRot)) / 90)));
                    }
                }
            });
        }

        if (player.getItemInHand(Hand.OFF_HAND).getItem() instanceof LauncherItem) {
            player.getItemInHand(Hand.OFF_HAND).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap->{
                if (cap.getStackInSlot(1).getItem() instanceof HandleItem){
                    if (cap.getStackInSlot(1).getItem() instanceof HandleItem){
                        if(player.getMainArm() !=HandSide.RIGHT){
                            model.rightArm.yRot = (float) Math.toRadians(0);
                            model.rightArm.zRot = (float) Math.toRadians(0);
                            model.rightSleeve.yRot = (float) Math.toRadians(0);
                            model.rightSleeve.zRot = (float) Math.toRadians(0);

                        } else {
                            model.leftArm.yRot = (float) Math.toRadians(0);
                            model.leftArm.zRot = (float) Math.toRadians(0);
                            model.leftSleeve.yRot = (float) Math.toRadians(0);
                            model.leftSleeve.zRot = (float) Math.toRadians(0);
                        }
                    }
                    if (player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())) {
                        if (player.getMainArm() != HandSide.RIGHT) {
                            model.leftArm.yRot = (float) Math.toRadians(-25);
                            model.leftArm.zRot = (float) Math
                                    .toRadians(-25 * (1 - ((90 - player.xRot) / 90)));
                            model.leftSleeve.yRot = (float) Math.toRadians(-25);
                            model.leftSleeve.zRot = (float) Math
                                    .toRadians(-25 * (1 - ((90 - player.xRot) / 90)));
                        } else {
                            model.rightArm.yRot = (float) Math.toRadians(25);
                            model.rightArm.zRot = (float) Math
                                    .toRadians(-25 * (1 - ((90 - (-player.xRot)) / 90)));
                            model.rightSleeve.yRot = (float) Math.toRadians(25);
                            model.rightSleeve.zRot = (float) Math
                                    .toRadians(-25 * (1 - ((90 - (-player.xRot)) / 90)));
                        }
                    }
                }
            });
        }
    }

    public static class SpecialEvents {

        @SubscribeEvent
        public void registerEntityType(final RegistryEvent.Register<EntityType<?>> event) {

            GlobalEntityTypeAttributes.put(ModEntities.BEYBLADE,
                    BeybladeEntity.registerMonsterAttributes().build());

        }
    }
}
