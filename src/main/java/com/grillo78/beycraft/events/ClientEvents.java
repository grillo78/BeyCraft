package com.grillo78.beycraft.events;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Random;
import java.util.function.Consumer;

import com.grillo78.beycraft.BeyCraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.BellTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.lwjgl.glfw.GLFW;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.capabilities.BladerCapProvider;
import com.grillo78.beycraft.entity.BeyEntityRenderFactory;
import com.grillo78.beycraft.gui.*;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageOpenBelt;
import com.grillo78.beycraft.network.message.MessagePlayCountdown;
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

		File itemsFolder = new File(Minecraft.getInstance().gameDir, "BeyParts");
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
					protected InputStream getInputStream(String resourcePath) throws IOException {
						if ("pack.mcmeta".equals(resourcePath))
							return new ByteArrayInputStream(
									("{\"pack\":{\"description\": \"" + description + "\",\"pack_format\": 5}}")
											.getBytes(StandardCharsets.UTF_8));
						if (!resourcePath.startsWith(prefix))
							throw new FileNotFoundException(resourcePath);

						return super.getInputStream(resourcePath);
					}

					@Override
					public boolean resourceExists(String resourcePath) {
						if ("pack.mcmeta".equals(resourcePath))
							return true;
						if (!resourcePath.startsWith(prefix))
							return false;

						return super.resourceExists(resourcePath);
					}
				};

				Minecraft.getInstance().getResourcePackList().addPackFinder(new IPackFinder() {
					@Override
					public void func_230230_a_(Consumer<ResourcePackInfo> p_230230_1_,
							ResourcePackInfo.IFactory p_230230_2_) {
						ResourcePackInfo t = ResourcePackInfo.createResourcePack(name, true, () -> pack, p_230230_2_,
								ResourcePackInfo.Priority.TOP, IPackNameDecorator.field_232625_a_);
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
		Minecraft.getInstance().particles.registerFactory(BeyRegistry.SPARKLE, SparkleParticle.Factory::new);
	}

	@SubscribeEvent
	public static void doClientStuff(final FMLClientSetupEvent event) {

		RenderTypeLookup.setRenderLayer(BeyRegistry.STADIUM, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(BeyRegistry.EXPOSITORY, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(BeyRegistry.BEYCREATORBLOCK, RenderType.getCutoutMipped());

		ClientRegistry.registerKeyBinding(ClientEvents.COUNTDOWNKEY);
		ScreenManager.registerFactory(BeyRegistry.LAUNCHER_RIGHT_CONTAINER, LauncherGUI::new);
		ScreenManager.registerFactory(BeyRegistry.LAUNCHER_LEFT_CONTAINER, LauncherGUI::new);
		ScreenManager.registerFactory(BeyRegistry.LAUNCHER_DUAL_CONTAINER, LauncherDualGUI::new);
		ScreenManager.registerFactory(BeyRegistry.DISC_FRAME_CONTAINER, DiskFrameGUI::new);
		ScreenManager.registerFactory(BeyRegistry.BELT_CONTAINER, BeltGUI::new);
		BELTKEY = new KeyBinding("key.beycraft.belt", 66, "key.beycraft.category");
		ClientRegistry.registerKeyBinding(ClientEvents.BELTKEY);
		ScreenManager.registerFactory(BeyRegistry.BEY_CREATOR_CONTAINER, BeyCreatorGUI::new);
		ScreenManager.registerFactory(BeyRegistry.BEYLOGGER_CONTAINER, BeyloggerGUI::new);
		if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
			if (BeyRegistry.ITEMSLAYER.size() != BeyRegistry.ITEMSLAYERGT.size()) {
				ScreenManager.registerFactory(BeyRegistry.BEY_CONTAINER, BeyGUI::new);
			}
			if (!BeyRegistry.ITEMSLAYERGT.isEmpty()) {
				ScreenManager.registerFactory(BeyRegistry.BEY_GT_CONTAINER, BeyGTGUI::new);
			}
			if (!BeyRegistry.ITEMSLAYERGTNOWEIGHT.isEmpty()) {
				ScreenManager.registerFactory(BeyRegistry.BEY_GT_CONTAINER_NO_WEIGHT, BeyGTNoWeightGUI::new);
			}
		}
		ScreenManager.registerFactory(BeyRegistry.HANDLE_CONTAINER, HandleGUI::new);
		for (Item item : BeyRegistry.ITEMSLAYER) {
			ModelLoader.addSpecialModel(new ResourceLocation("beycraft",
					"layers/" + item.getTranslationKey().replace("item.beycraft.", "")));
		}
		for (Item item : BeyRegistry.ITEMSDISCFRAME) {
			ModelLoader.addSpecialModel(new ResourceLocation("beycraft",
					"discsframe/" + item.getTranslationKey().replace("item.beycraft.", "")));
		}
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
				+ BeyRegistry.DUALLAUNCHER.getTranslationKey().replace("item.beycraft.", "") + "/launcher_body"));
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
				+ BeyRegistry.DUALLAUNCHER.getTranslationKey().replace("item.beycraft.", "") + "/grab_part"));
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
				+ BeyRegistry.DUALLAUNCHER.getTranslationKey().replace("item.beycraft.", "") + "/launcher_lever"));
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
				+ BeyRegistry.LAUNCHER.getTranslationKey().replace("item.beycraft.", "") + "/launcher_body"));
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft",
				"launchers/" + BeyRegistry.LAUNCHER.getTranslationKey().replace("item.beycraft.", "") + "/grab_part"));
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
				+ BeyRegistry.LEFTLAUNCHER.getTranslationKey().replace("item.beycraft.", "") + "/launcher_body"));
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "launchers/"
				+ BeyRegistry.LEFTLAUNCHER.getTranslationKey().replace("item.beycraft.", "") + "/grab_part"));
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft",
				"beyloggers/" + BeyRegistry.BEYLOGGERPLUS.getTranslationKey().replace("item.beycraft.", "")));
		ModelLoader.addSpecialModel(new ResourceLocation("beycraft",
				"beyloggers/" + BeyRegistry.BEYLOGGER.getTranslationKey().replace("item.beycraft.", "")));
		RenderingRegistry.registerEntityRenderingHandler(BeyRegistry.BEY_ENTITY_TYPE, new BeyEntityRenderFactory());
		ClientRegistry.bindTileEntityRenderer(BeyRegistry.EXPOSITORYTILEENTITYTYPE, RenderExpository::new);
		ClientRegistry.bindTileEntityRenderer(BeyRegistry.BEYCREATORTILEENTITYTYPE, RenderBeyCreator::new);
		ClientRegistry.bindTileEntityRenderer(BeyRegistry.ROBOTTILEENTITYTYPE, RenderRobot::new);
	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class SpecialClientEvents {

		@SubscribeEvent
		public static void onScreenOpen(final GuiOpenEvent event) {
			if (event.getGui() instanceof MainMenuScreen && firstScreenMenuOpen) {
				firstScreenMenuOpen = false;
				DiscordRPC.discordInitialize("736594360149344267",
						new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
							System.out
									.println("Discord RPC is ready for user: " + user.username + "#" + user.discriminator);
						}).build(), true);
				BeyCraft.TIME_STAMP = new Timestamp(System.currentTimeMillis()).getTime();
				setDiscordRPC();
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						DiscordRPC.discordShutdown();
					}
				});
				File[] zipFiles = new File("BeyParts").listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".zip");
					}
				});
				if (zipFiles.length == 0) {
					event.setCanceled(true);
					Minecraft.getInstance().displayGuiScreen(new MissingContentPacksScreen(new StringTextComponent("")));
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
			if (!Minecraft.getInstance().gameSettings.showDebugInfo) {
				if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
					Minecraft.getInstance().player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
						String s = String.valueOf(h.getBladerLevel());
						float i1 = (75 - Minecraft.getInstance().fontRenderer.getStringWidth(s)) / 2f;
						int j1 = 16;
						Minecraft.getInstance().fontRenderer.drawStringWithShadow(event.getMatrixStack(), s, i1, (float) j1,
								8453920);
						s = "Blader Level:";

						i1 = 5;
						j1 = 5;
						Minecraft.getInstance().fontRenderer.drawStringWithShadow(event.getMatrixStack(), s, i1, (float) j1,
								8453920);
						Minecraft.getInstance().getRenderManager().textureManager
								.bindTexture(AbstractGui.GUI_ICONS_LOCATION);
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
			if (ClientEvents.BELTKEY.isPressed()) {
				PacketHandler.instance.sendToServer(new MessageOpenBelt());
			}
			if (ClientEvents.COUNTDOWNKEY.isPressed() && event.getAction() == GLFW.GLFW_PRESS) {
				PacketHandler.instance.sendToServer(new MessagePlayCountdown());
			}
		}

		@SubscribeEvent
		public static void onPlayerExitsWorld(final PlayerEvent.PlayerLoggedOutEvent event) {
			if (event.getPlayer().getName().equals(Minecraft.getInstance().player.getName())) {
				setDiscordRPC();
			}
		}
	}
}
