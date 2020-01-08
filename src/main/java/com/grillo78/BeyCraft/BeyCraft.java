package com.grillo78.BeyCraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.grillo78.BeyCraft.entity.BeyEntityRenderFactory;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.gui.LauncherGUI;
import com.grillo78.BeyCraft.inventory.LauncherContainer;
import com.grillo78.BeyCraft.tab.BeyCraftDisksTab;
import com.grillo78.BeyCraft.tab.BeyCraftDriversTab;
import com.grillo78.BeyCraft.tab.BeyCraftLayersTab;
import com.grillo78.BeyCraft.tab.BeyCraftTab;
import com.mojang.brigadier.Message;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MODID)
public class BeyCraft {

	// Directly reference a log4j logger.
	public static final Logger logger = LogManager.getLogger();

	public static final DeferredRegister<EntityType<?>> ENTITY = new DeferredRegister<>(ForgeRegistries.ENTITIES,
			Reference.MODID);
	public static final RegistryObject<EntityType<EntityBey>> BEY_ENTITY_TYPE = ENTITY.register("bey",
			() -> EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC).build(Reference.MODID + ":bey"));
	@ObjectHolder("beycraft:launcher")
    public static ContainerType<LauncherContainer> LAUNCHER_CONTAINER;
	public static final ItemGroup BEYCRAFTLAYERS = new BeyCraftLayersTab("Layers");
	public static final ItemGroup BEYCRAFTDISKS = new BeyCraftDisksTab("Disks");
	public static final ItemGroup BEYCRAFTDRIVERS = new BeyCraftDriversTab("Drivers");
	public static final ItemGroup BEYCRAFTTAB = new BeyCraftTab("Beycraft");

	public BeyCraft() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class, this::registerEntityType);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerEntityRender);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void registerEntityRender(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(BEY_ENTITY_TYPE.get(), new BeyEntityRenderFactory());
	}

	private void registerEntityType(RegistryEvent.Register<EntityType<?>> event) {
		EntityType<?> type = EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC).size(0.25F, 0.25F)
				.build(Reference.MODID + ":bey").setRegistryName(Reference.MODID, "bey");
		event.getRegistry().register(type);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(BeyRegistry.STADIUM, RenderType.func_228641_d_());
		ScreenManager.registerFactory(LAUNCHER_CONTAINER, LauncherGUI::new);
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

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new LauncherContainer(LAUNCHER_CONTAINER, windowId, new ItemStack(BeyRegistry.REDLAUNCHER), inv, inv.player,1);
            }).setRegistryName("launcher"));
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
			for (Item item : BeyRegistry.ITEMS) {
				event.getRegistry().register(item);
			}
			for (Item item : BeyRegistry.ITEMSLAYER) {
				event.getRegistry().register(item);
			}
			for (Item item : BeyRegistry.ITEMSDISK) {
				event.getRegistry().register(item);
			}
			for (Item item : BeyRegistry.ITEMSDRIVER) {
				event.getRegistry().register(item);
			}
		}

		@SubscribeEvent
		public static void playerJoined(PlayerLoggedInEvent event) {
			PlayerEntity player = event.getPlayer();
			ITextComponent prefix = TextComponentUtils.toTextComponent(new Message() {

				@Override
				public String getString() {
					// TODO Auto-generated method stub
					return "[BeyCraft] -> Join to my Discord server: ";
				}
			});
			ITextComponent url = TextComponentUtils.toTextComponent(new Message() {

				@Override
				public String getString() {
					// TODO Auto-generated method stub
					return "https://discord.gg/2PpbtFr";
				}
			});
			Style sPrefix = new Style();
			sPrefix.setColor(TextFormatting.GOLD);
			Style sUrl = new Style();
			sUrl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/2PpbtFr"))
					.setColor(TextFormatting.GOLD);
			prefix.setStyle(sPrefix);
			url.setStyle(sUrl);
			player.sendMessage(prefix);
			player.sendMessage(url);
//			BeyCraft.INSTANCE.sendTo(
//					new BladerLevelMessage(
//							(int) event.player.getCapability(Provider.BLADERLEVEL_CAP, null).getBladerLevel()),
//					(PlayerEntityMP) event.player);
		}

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
