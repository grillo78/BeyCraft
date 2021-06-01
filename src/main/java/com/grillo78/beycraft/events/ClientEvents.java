package com.grillo78.beycraft.events;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Random;
import java.util.function.Consumer;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.entity.BeyRender;
import com.grillo78.beycraft.items.ItemLauncher;
import com.grillo78.beycraft.items.ItemLauncherHandle;
import com.grillo78.beycraft.items.render.BeyItemStackRendererTileEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.math.Matrix4f;
import friedrichlp.renderlib.math.Vector3;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.lwjgl.glfw.GLFW;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.capabilities.BladerCapProvider;
import com.grillo78.beycraft.entity.BeyEntityRenderFactory;
import com.grillo78.beycraft.gui.*;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageOpenBelt;
import com.grillo78.beycraft.network.message.MessageNotifyPlayCountdown;
import com.grillo78.beycraft.particles.SparkleParticle;
import com.grillo78.beycraft.tileentity.RenderBeyCreator;
import com.grillo78.beycraft.tileentity.RenderExpository;
import com.grillo78.beycraft.tileentity.RenderRobot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.system.MemoryUtil;
import xyz.heroesunited.heroesunited.client.events.HUSetRotationAnglesEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    public static KeyBinding BELTKEY;
    public static final KeyBinding COUNTDOWNKEY = new KeyBinding("key.beycraft.countdown", 67, "key.beycraft.category");
    private static boolean firstScreenMenuOpen = true;
    private static Random random = new Random();

    public static void injectResources() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null)
            return;

        File itemsFolder = new File(Minecraft.getInstance().gameDirectory, "BeyParts");
        if (!itemsFolder.exists()) {
            itemsFolder.mkdir();
        }
        File[] zipFiles = itemsFolder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".zip");
            }
        });
        for (File file : zipFiles) {
            try {
                final String id = file.getName().replace(".zip", "").replace(" ", "_") + "_addon_resources";
                final String name = file.getName().replace(".zip", "") + " Resources";
                final String description = "Beycraft addon resources.";

                final IResourcePack pack = new FilePack(file) {
                    String prefix = "assets/beycraft";

                    @Override
                    protected InputStream getResource(String resourcePath) throws IOException {
                        if ("pack.mcmeta".equals(resourcePath))
                            return new ByteArrayInputStream(
                                    ("{\"pack\":{\"description\": \"" + description + "\",\"pack_format\": 5}}")
                                            .getBytes(StandardCharsets.UTF_8));
                        if (!resourcePath.startsWith(prefix))
                            throw new FileNotFoundException(resourcePath);

                        return super.getResource(resourcePath);
                    }

                    @Override
                    public boolean hasResource(String resourcePath) {
                        if ("pack.mcmeta".equals(resourcePath))
                            return true;
                        if (!resourcePath.startsWith(prefix))
                            return false;

                        return super.hasResource(resourcePath);
                    }
                };

                Minecraft.getInstance().getResourcePackRepository().addPackFinder(new IPackFinder() {
                    @Override
                    public void loadPacks(Consumer<ResourcePackInfo> p_230230_1_,
                                          ResourcePackInfo.IFactory p_230230_2_) {
                        ResourcePackInfo t = ResourcePackInfo.create(name, true, () -> pack, p_230230_2_,
                                ResourcePackInfo.Priority.TOP, IPackNameDecorator.DEFAULT);
                        if (t != null) {
                            p_230230_1_.accept(t);
                        }
                    }
                });
            } catch (Exception e) {

            }
        }

    }

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
        ScreenManager.register(BeyRegistry.LAUNCHER_RIGHT_CONTAINER, LauncherGUI::new);
        ScreenManager.register(BeyRegistry.LAUNCHER_LEFT_CONTAINER, LauncherGUI::new);
        ScreenManager.register(BeyRegistry.LAUNCHER_DUAL_CONTAINER, LauncherDualGUI::new);
        ScreenManager.register(BeyRegistry.DISC_FRAME_CONTAINER, DiskFrameGUI::new);
        ScreenManager.register(BeyRegistry.BELT_CONTAINER, BeltGUI::new);
        BELTKEY = new KeyBinding("key.beycraft.belt", 66, "key.beycraft.category");
        ClientRegistry.registerKeyBinding(ClientEvents.BELTKEY);
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
    public static void modelBake(ModelBakeEvent event){
        for (Item item : BeyRegistry.ITEMSLAYER) {
            event.getModelRegistry().put(new ModelResourceLocation(new ResourceLocation(Reference.MODID,"item/"+item.getRegistryName().getPath()), "inventory"), new BuiltInModel(new ItemCameraTransforms(ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM, ItemTransformVec3f.NO_TRANSFORM), ItemOverrideList.EMPTY, MissingTextureSprite.newInstance(new AtlasTexture(PlayerContainer.BLOCK_ATLAS),0,10,10,0,0), true));
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

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class SpecialClientEvents {

        @SubscribeEvent
        public static void onRenderWorldLast(RenderWorldLastEvent event){

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            RenderManager.setCameraPos(new Vector3((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z));
            RenderManager.setRenderDistance(Minecraft.getInstance().options.renderDistance * 16);
            RenderManager.update();
            for (Runnable runnable : BeyRender.getRunnables()) {
                runnable.run();
            }
            BeyRender.getRunnables().clear();
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
                }
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
        }

        @SubscribeEvent
        public static void onPlayerExitsWorld(final PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getPlayer().getName().equals(Minecraft.getInstance().player.getName())) {
                setDiscordRPC();
            }
        }

        @SubscribeEvent
        public static void setPlayerRotations(final HUSetRotationAnglesEvent event) {
            PlayerEntity player = event.getPlayer();
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemLauncher
                    || player.getItemInHand(Hand.OFF_HAND).getItem() instanceof ItemLauncher) {
                PlayerModel model = (PlayerModel) event.getPlayerModel();
                model.leftArm.xRot = (float) Math.toRadians(player.xRot - 90);
                model.rightArm.xRot = (float) Math.toRadians(player.xRot - 90);

                model.leftSleeve.xRot = (float) Math.toRadians(player.xRot - 90);
                model.rightSleeve.xRot = (float) Math.toRadians(player.xRot - 90);

                model.leftArm.yRot = (float) Math.toRadians(25);
                model.rightArm.yRot = (float) Math.toRadians(-25);
                model.rightArm.zRot = (float) Math
                        .toRadians(25 * (1 - ((90 - (-player.xRot)) / 90)));
                model.leftArm.zRot = (float) Math.toRadians(25 * (1 - ((90 - player.xRot) / 90)));

                model.leftSleeve.yRot = (float) Math.toRadians(25);
                model.rightSleeve.yRot = (float) Math.toRadians(-25);
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

            }
        }
    }
}
