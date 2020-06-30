package com.grillo78.beycraft.events;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.capabilities.BladerLevel;
import com.grillo78.beycraft.capabilities.BladerLevelProvider;
import com.grillo78.beycraft.entity.BeyEntityRenderFactory;
import com.grillo78.beycraft.gui.*;
import com.grillo78.beycraft.items.ItemLauncher;
import com.grillo78.beycraft.items.ItemLauncherHandle;
import com.grillo78.beycraft.items.render.BeyLoggerItemStackRendererTileEntity;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageOpenBelt;
import com.grillo78.beycraft.particles.SparkleParticle;
import com.grillo78.beycraft.tileentity.RenderBeyCreator;
import com.grillo78.beycraft.tileentity.RenderExpository;
import com.grillo78.beycraft.tileentity.RenderRobot;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.*;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.annotation.Native;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

	public static KeyBinding BELTKEY;
	public static final KeyBinding COUNTDOWNKEY = new KeyBinding("key.beycraft.countdown", 67, "key.beycraft.category");

	public static void injectResources() {
		Minecraft mc = Minecraft.getInstance();
		if (mc == null)
			return;

		File itemsFolder = new File(Minecraft.getInstance().gameDir, "BeyParts");
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
									("{\"pack\":{\"description\": \""+description+"\",\"pack_format\": 5}}")
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
					public <T extends ResourcePackInfo> void func_230230_a_(Consumer<T> p_230230_1_,
							ResourcePackInfo.IFactory<T> p_230230_2_) {
						T t = ResourcePackInfo.createResourcePack(name, true, () -> pack,
								p_230230_2_, ResourcePackInfo.Priority.TOP, IPackNameDecorator.field_232625_a_);
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
		try {
			Class.forName("com.lazy.baubles.Baubles");
			ScreenManager.registerFactory(BeyRegistry.BELT_CONTAINER, BeltGUI::new);
			BELTKEY = new KeyBinding("key.beycraft.belt", 66, "key.beycraft.category");
			ClientRegistry.registerKeyBinding(ClientEvents.BELTKEY);
		} catch (Exception e) {
		}
		ScreenManager.registerFactory(BeyRegistry.BEY_CREATOR_CONTAINER, BeyCreatorGUI::new);
		ScreenManager.registerFactory(BeyRegistry.BEYLOGGER_CONTAINER, BeyloggerGUI::new);
		if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
			if (BeyRegistry.ITEMSLAYER.size() != BeyRegistry.ITEMSLAYERGT.size()) {
				ScreenManager.registerFactory(BeyRegistry.BEY_CONTAINER, BeyGUI::new);
			}
			if (!BeyRegistry.ITEMSLAYERGT.isEmpty()) {
				ScreenManager.registerFactory(BeyRegistry.BEY_GT_CONTAINER, BeyGTGUI::new);
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
		public static void editHud(RenderGameOverlayEvent.Post event) {
			if (!Minecraft.getInstance().gameSettings.showDebugInfo) {
				if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
					Minecraft.getInstance().player.getCapability(BladerLevelProvider.BLADERLEVEL_CAP).ifPresent(h -> {
						String s = String.valueOf(h.getBladerLevel());
						float i1 = (75 - Minecraft.getInstance().fontRenderer.getStringWidth(s)) / 2f;
						int j1 = 16;
						Minecraft.getInstance().fontRenderer.func_238405_a_(event.getMatrixStack(), s, i1, (float) j1,
								8453920);
						s = "Blader Level:";

						i1 = 5;
						j1 = 5;
						Minecraft.getInstance().fontRenderer.func_238405_a_(event.getMatrixStack(), s, i1, (float) j1,
								8453920);
						Minecraft.getInstance().getRenderManager().textureManager
								.bindTexture(AbstractGui.field_230665_h_);
						float i = h.getExperience() / (h.getExpForNextLevel() + h.getExperience());
						AbstractGui.func_238463_a_(event.getMatrixStack(), 1, 27, 0, 74, 75, 5, 105, 256);
						AbstractGui.func_238463_a_(event.getMatrixStack(), 1, 27, 0, 79, (int) (i * 75), 5, 105, 256);
					});
				}
			}
		}

		@SubscribeEvent
		public static void onKeyPressed(final InputEvent.KeyInputEvent event) {
			if (Minecraft.getInstance().player == null)
				return;

			try {
				Class.forName("com.lazy.baubles.Baubles");
				if (ClientEvents.BELTKEY.isPressed()) {
					PacketHandler.instance.sendToServer(new MessageOpenBelt());
				}
			} catch (Exception e) {

			}
			if (ClientEvents.COUNTDOWNKEY.isPressed()) {
				Narrator.getNarrator().say(new TranslationTextComponent("text.countdown").getString(), false);
			}
		}


		@SubscribeEvent
		public static void onSetupAngles(RenderPlayerEvent.Pre event) {
			PlayerEntity player = event.getPlayer();

			event.isCancelable();

		}
	}
}
