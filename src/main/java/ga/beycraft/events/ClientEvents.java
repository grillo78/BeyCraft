package ga.beycraft.events;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.RenderLibSettings;
import friedrichlp.renderlib.math.Vector3;
import friedrichlp.renderlib.tracking.RenderManager;
import ga.beycraft.BeyCraft;
import ga.beycraft.BeyRegistry;
import ga.beycraft.Reference;
import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.entity.BeyEntityRenderFactory;
import ga.beycraft.entity.BeyRender;
import ga.beycraft.gui.*;
import ga.beycraft.items.ItemLauncher;
import ga.beycraft.items.ItemLauncherHandle;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageNotifyPlayCountdown;
import ga.beycraft.network.message.MessageOpenBelt;
import ga.beycraft.particles.SparkleParticle;
import ga.beycraft.tileentity.RenderBeyCreator;
import ga.beycraft.tileentity.RenderExpository;
import ga.beycraft.tileentity.RenderRobot;
import ga.beycraft.util.BeyPartModel;
import ga.beycraft.util.ConfigManager;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import xyz.heroesunited.heroesunited.client.events.HUSetRotationAnglesEvent;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Timestamp;
import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    public static KeyBinding BELTKEY = new KeyBinding("key.beycraft.belt", 66, "key.beycraft.category");
    public static final KeyBinding COUNTDOWNKEY = new KeyBinding("key.beycraft.countdown", 67, "key.beycraft.category");
    public static final KeyBinding RANKINGKEY = new KeyBinding("key.beycraft.ranking", 82, "key.beycraft.category");
    private static boolean firstScreenMenuOpen = true;
    private static Random random = new Random();

    @SubscribeEvent
    public static void onParticleFactorieRegistry(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(BeyRegistry.SPARKLE, SparkleParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(BeyRegistry.AURA, SparkleParticle.Factory::new);
    }

    @SubscribeEvent
    public static void doClientStuff(final FMLClientSetupEvent event) {

        RenderTypeLookup.setRenderLayer(BeyRegistry.STADIUM, RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(BeyRegistry.EXPOSITORY, RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(BeyRegistry.BEYCREATORBLOCK, RenderType.cutoutMipped());

        ClientRegistry.registerKeyBinding(ClientEvents.COUNTDOWNKEY);
        ClientRegistry.registerKeyBinding(ClientEvents.BELTKEY);
        ClientRegistry.registerKeyBinding(ClientEvents.RANKINGKEY);
        ScreenManager.register(BeyRegistry.LAUNCHER_RIGHT_CONTAINER, LauncherGUI::new);
        ScreenManager.register(BeyRegistry.LAUNCHER_LEFT_CONTAINER, LauncherGUI::new);
        ScreenManager.register(BeyRegistry.LAUNCHER_DUAL_CONTAINER, LauncherDualGUI::new);
        ScreenManager.register(BeyRegistry.DISC_FRAME_CONTAINER, DiskFrameGUI::new);
        ScreenManager.register(BeyRegistry.BELT_CONTAINER, BeltGUI::new);
        ScreenManager.register(BeyRegistry.BEYLOGGER_CONTAINER, BeyloggerGUI::new);
        if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
            if (BeyRegistry.ITEMSLAYER.size() != BeyRegistry.ITEMSLAYERGT.size()) {
                ScreenManager.register(BeyRegistry.BEY_CONTAINER, BeyGUI::new);
            }
            if (!BeyRegistry.ITEMSLAYERGT.isEmpty()) {
                ScreenManager.register(BeyRegistry.BEY_GT_CONTAINER, BeyGTGUI::new);
            }
            if (!BeyRegistry.ITEMSLAYERGTNOWEIGHT.isEmpty()) {
                ScreenManager.register(BeyRegistry.BEY_GT_CONTAINER_NO_WEIGHT, BeyGTNoWeightGUI::new);
            }
            if (!BeyRegistry.ITEMSLAYERGOD.isEmpty()) {
                ScreenManager.register(BeyRegistry.BEY_GOD_CONTAINER, BeyGodGUI::new);
            }
        }
        ScreenManager.register(BeyRegistry.HANDLE_CONTAINER, HandleGUI::new);
        RenderingRegistry.registerEntityRenderingHandler(BeyRegistry.BEY_ENTITY_TYPE, new BeyEntityRenderFactory());
        ClientRegistry.bindTileEntityRenderer(BeyRegistry.EXPOSITORYTILEENTITYTYPE, RenderExpository::new);
        ClientRegistry.bindTileEntityRenderer(BeyRegistry.BEYCREATORTILEENTITYTYPE, RenderBeyCreator::new);
        ClientRegistry.bindTileEntityRenderer(BeyRegistry.ROBOTTILEENTITYTYPE, RenderRobot::new);
    }

    @SubscribeEvent
    public static void modelBake(ModelBakeEvent event) {
        for (Item item : BeyRegistry.ITEMSLAYER) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
            event.getModelRegistry().put(modelResourceLocation, new BuiltInModel(new ItemCameraTransforms(ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM), ItemOverrideList.EMPTY, MissingTextureSprite.newInstance(new AtlasTexture(PlayerContainer.BLOCK_ATLAS), 0, 10, 10, 0, 0), true));
        }
        for (Item item : BeyRegistry.ITEMSFRAME) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
            event.getModelRegistry().put(modelResourceLocation, new BuiltInModel(new ItemCameraTransforms(ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM), ItemOverrideList.EMPTY, MissingTextureSprite.newInstance(new AtlasTexture(PlayerContainer.BLOCK_ATLAS), 0, 10, 10, 0, 0), true));
        }
        for (Item item : BeyRegistry.ITEMSDISCLIST) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
            event.getModelRegistry().put(modelResourceLocation, new BuiltInModel(new ItemCameraTransforms(ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM), ItemOverrideList.EMPTY, MissingTextureSprite.newInstance(new AtlasTexture(PlayerContainer.BLOCK_ATLAS), 0, 10, 10, 0, 0), true));
        }
        for (Item item : BeyRegistry.ITEMSDRIVER) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
            event.getModelRegistry().put(modelResourceLocation, new BuiltInModel(new ItemCameraTransforms(ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM), ItemOverrideList.EMPTY, MissingTextureSprite.newInstance(new AtlasTexture(PlayerContainer.BLOCK_ATLAS), 0, 10, 10, 0, 0), true));
        }
    }

    @SubscribeEvent
    public static void registerModel(final ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
                + BeyRegistry.DUALLAUNCHER.getRegistryName().getPath() + "/launcher_body"));
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
                + BeyRegistry.DUALLAUNCHER.getRegistryName().getPath() + "/grab_part"));
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
                + BeyRegistry.DUALLAUNCHER.getRegistryName().getPath() + "/launcher_lever"));
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
                + BeyRegistry.LAUNCHER.getRegistryName().getPath() + "/launcher_body"));
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft",
                "launchers/" + BeyRegistry.LAUNCHER.getRegistryName().getPath() + "/grab_part"));
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
                + BeyRegistry.LEFTLAUNCHER.getRegistryName().getPath() + "/launcher_body"));
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
                + BeyRegistry.LEFTLAUNCHER.getRegistryName().getPath() + "/grab_part"));
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft",
                "beyloggers/" + BeyRegistry.BEYLOGGERPLUS.getRegistryName().getPath()));
        ModelLoader.addSpecialModel(new ResourceLocation("beycraft",
                "beyloggers/" + BeyRegistry.BEYLOGGER.getRegistryName().getPath()));
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class SpecialClientEvents {

        @SubscribeEvent
        public static void onRenderWorldLast(RenderWorldLastEvent event) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            RenderManager.setCameraPos(new Vector3((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z));
            RenderLibSettings.Rendering.CAMERA_HEIGHT_OFFSET = (float) (cameraPos.y - Minecraft.getInstance().player.getPosition(event.getPartialTicks()).y);
            RenderManager.setRenderDistance(Minecraft.getInstance().options.renderDistance * 16);
            try {
                RenderManager.update();
            } catch (NullPointerException e){}
            for (Runnable runnable : BeyRender.getRunnables()) {
                runnable.run();
            }
            BeyPartModel.worldModels.forEach(h -> h.render());
            BeyPartModel.worldModels.clear();
            BeyRender.getRunnables().clear();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }

        @SubscribeEvent
        public static void renderHand(RenderHandEvent event) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            BeyPartModel.handModels.forEach(h -> h.render());
            BeyPartModel.handModels.clear();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }

        @SubscribeEvent
        public static void onScreenOpen(final GuiOpenEvent event) {
            if (event.getGui() instanceof MainMenuScreen && firstScreenMenuOpen) {
                firstScreenMenuOpen = false;
                try {
                    DiscordRPC.discordInitialize("736594360149344267",
                            new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
                                BeyCraft.logger.info(
                                        "Discord RPC is ready for user: " + user.username + "#" + user.discriminator);
                            }).build(), true);
                    BeyCraft.TIME_STAMP = new Timestamp(System.currentTimeMillis()).getTime();
                    setDiscordRPC();
                } catch (Exception e) {
                    BeyCraft.logger.error("Error during Discord RPC start");
                }
                File[] propertiesFiles = new File("BeyParts").listFiles(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".properties");
                    }
                });
                if (propertiesFiles.length == 0) {
                    event.setCanceled(true);
                    Minecraft.getInstance()
                            .setScreen(new MissingContentPacksScreen(new StringTextComponent("")));
                } else ConfigManager.checkLogIn(event);
            }
        }

        public static void setDiscordRPC() {
            DiscordRichPresence rich = new DiscordRichPresence.Builder("In the menus").setBigImage("beycraft", "")
                    .setStartTimestamps(BeyCraft.TIME_STAMP).build();
            DiscordRPC.discordUpdatePresence(rich);
        }

        @SubscribeEvent
        public static void editHud(RenderGameOverlayEvent.Post event) {
            if (!Minecraft.getInstance().options.renderDebug) {
                if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                    Minecraft.getInstance().player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
                        String s = String.valueOf(h.getBladerLevel());
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
                        float i = h.getExperience() / (h.getExpForNextLevel() + h.getExperience());
                        AbstractGui.blit(event.getMatrixStack(), 1, 27, 0, 74, 75, 5, 105, 256);
                        AbstractGui.blit(event.getMatrixStack(), 1, 27, 0, 79, (int) (i * 75), 5, 105, 256);
                    });
                }
            }
        }

        @SubscribeEvent
        public static void onKeyPressed(final InputEvent.KeyInputEvent event) {
            if (Minecraft.getInstance().player == null)
                return;
            if (ClientEvents.BELTKEY.consumeClick()) {
                PacketHandler.instance.sendToServer(new MessageOpenBelt());
            }
            if (ClientEvents.COUNTDOWNKEY.consumeClick() && event.getAction() == GLFW.GLFW_PRESS) {
                PacketHandler.instance.sendToServer(new MessageNotifyPlayCountdown());
            }
            if (ClientEvents.RANKINGKEY.consumeClick()){
                Minecraft.getInstance()
                        .setScreen(new RankingScreen(new StringTextComponent("")));
            }
        }

        @SubscribeEvent
        public static void onPlayerExitsWorld(final PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getPlayer().getName().equals(Minecraft.getInstance().player.getName())) {
                setDiscordRPC();
            }
        }

        @SubscribeEvent
        public static void setPlayerRotations(final HUSetRotationAnglesEvent event) {
//            if (!(event.getPlayer() == Minecraft.getInstance().player && !Minecraft.getInstance().gameRenderer.getMainCamera().isDetached())) {
            PlayerEntity player = event.getPlayer();
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemLauncher
                    || player.getItemInHand(Hand.OFF_HAND).getItem() instanceof ItemLauncher) {
                PlayerModel model = (PlayerModel) event.getPlayerModel();
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

                if (player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemLauncher) {
                    if (player.getItemInHand(Hand.MAIN_HAND).hasTag()
                            && (player.getItemInHand(Hand.MAIN_HAND).getTag().contains("handle")
                            && ItemStack.of(player.getItemInHand(Hand.MAIN_HAND).getTag().getCompound("handle"))
                            .getItem() instanceof ItemLauncherHandle)) {
                        if (player.getMainArm() == HandSide.RIGHT) {
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
                    if (player.getCooldowns().isOnCooldown(player.getItemInHand(Hand.MAIN_HAND).getItem())) {
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
                } else {
                    if (player.getItemInHand(Hand.OFF_HAND).hasTag()
                            && (player.getItemInHand(Hand.OFF_HAND).getTag().contains("handle")
                            && ItemStack.of(player.getItemInHand(Hand.OFF_HAND).getTag().getCompound("handle"))
                            .getItem() instanceof ItemLauncherHandle)) {
                        if (player.getMainArm() == HandSide.RIGHT) {
                            model.leftArm.yRot = (float) Math.toRadians(0);
                            model.leftArm.zRot = (float) Math.toRadians(0);
                            model.leftSleeve.yRot = (float) Math.toRadians(0);
                            model.leftSleeve.zRot = (float) Math.toRadians(0);
                        } else {
                            model.rightArm.yRot = (float) Math.toRadians(0);
                            model.rightArm.zRot = (float) Math.toRadians(0);
                            model.rightSleeve.yRot = (float) Math.toRadians(0);
                            model.rightSleeve.zRot = (float) Math.toRadians(0);
                        }
                    }
                    if (player.getCooldowns().isOnCooldown(player.getItemInHand(Hand.OFF_HAND).getItem())) {
                        if (player.getMainArm() == HandSide.RIGHT) {
                            model.rightArm.yRot = (float) Math.toRadians(25);
                            model.rightArm.zRot = (float) Math
                                    .toRadians(-25 * (1 - ((90 - (-player.xRot)) / 90)));
                            model.rightSleeve.yRot = (float) Math.toRadians(25);
                            model.rightSleeve.zRot = (float) Math
                                    .toRadians(-25 * (1 - ((90 - (-player.xRot)) / 90)));
                        } else {
                            model.leftArm.yRot = (float) Math.toRadians(-25);
                            model.leftArm.zRot = (float) Math
                                    .toRadians(-25 * (1 - ((90 - player.xRot) / 90)));
                            model.leftSleeve.yRot = (float) Math.toRadians(-25);
                            model.leftSleeve.zRot = (float) Math
                                    .toRadians(-25 * (1 - ((90 - player.xRot) / 90)));
                        }
                    }
                }
//                }
            }
        }
    }
}
