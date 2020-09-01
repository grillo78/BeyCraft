package com.grillo78.beycraft.events;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.capabilities.BladerCapProvider;
import com.grillo78.beycraft.commands.BeyBoxCommand;
import com.grillo78.beycraft.commands.BeyCoinsCommand;
import com.grillo78.beycraft.commands.SetLevelCommand;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.gui.BeyGTNoWeightGUI;
import com.grillo78.beycraft.inventory.*;
import com.grillo78.beycraft.inventory.slots.BeyLoggerContainer;
import com.grillo78.beycraft.items.ItemBladerBelt;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageSyncBladerLevel;
import com.grillo78.beycraft.tileentity.BeyCreatorTileEntity;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;
import com.grillo78.beycraft.tileentity.RobotTileEntity;
import com.grillo78.beycraft.tileentity.StadiumTileEntity;
import com.grillo78.beycraft.util.ItemCreator;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {

	@SubscribeEvent
	public static void registerEntityType(final RegistryEvent.Register<EntityType<?>> event) {
		EntityType<?> type = EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC)
				.disableSummoning().size(0.19F, 0.25F).build(Reference.MODID + ":bey");
		type.setRegistryName(Reference.MODID, "bey");
		event.getRegistry().register(type);

		GlobalEntityTypeAttributes.put((EntityType<? extends EntityBey>) type,
				EntityBey.registerAttributes().create());

	}

	@SubscribeEvent
	public static void registerTileEntityType(final RegistryEvent.Register<TileEntityType<?>> event) {
		event.getRegistry().register(TileEntityType.Builder.create(ExpositoryTileEntity::new, BeyRegistry.EXPOSITORY)
				.build(null).setRegistryName(new ResourceLocation(Reference.MODID, "expositorytileentity")));
		event.getRegistry()
				.register(TileEntityType.Builder.create(BeyCreatorTileEntity::new, BeyRegistry.BEYCREATORBLOCK)
						.build(null).setRegistryName(new ResourceLocation(Reference.MODID, "beycreatortileentity")));
		event.getRegistry().register(TileEntityType.Builder.create(RobotTileEntity::new, BeyRegistry.ROBOT).build(null)
				.setRegistryName(new ResourceLocation(Reference.MODID, "robottileentity")));
		event.getRegistry().register(TileEntityType.Builder.create(StadiumTileEntity::new, BeyRegistry.STADIUM)
				.build(null).setRegistryName(new ResourceLocation(Reference.MODID, "stadiumtileentity")));
	}

	@SubscribeEvent
	public static void onParticleTypeRegistry(final RegistryEvent.Register<ParticleType<?>> event) {
		event.getRegistry().register(new BasicParticleType(false).setRegistryName("sparkle"));
	}

	@SubscribeEvent
	public static void onSoundRegistry(final RegistryEvent.Register<SoundEvent> event) {
		BeyRegistry.HITSOUND.setRegistryName("bey.hit");
		event.getRegistry().register(BeyRegistry.HITSOUND);
		BeyRegistry.OPEN_CLOSE_BELT.setRegistryName("open_close_belt");
		event.getRegistry().register(BeyRegistry.OPEN_CLOSE_BELT);
	}

	@SubscribeEvent
	public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
		if (!BeyRegistry.ITEMSDISCFRAME.isEmpty()) {
			event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
				return new BeyDiscFrameContainer(windowId, new ItemStack(BeyRegistry.LAUNCHER), inv);
			}).setRegistryName("discframe"));
		}
		if(!BeyRegistry.ITEMSLAYERGOD.isEmpty()){
			event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
				return new BeyGodContainer(BeyRegistry.BEY_GOD_CONTAINER, windowId, new ItemStack(BeyRegistry.ITEMSLAYERGOD.get(0)), inv);
			}).setRegistryName("beygod"));
		}
		event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
			return new LauncherContainer(BeyRegistry.LAUNCHER_RIGHT_CONTAINER, windowId,
					new ItemStack(BeyRegistry.LAUNCHER), inv, Hand.MAIN_HAND);
		}).setRegistryName("right_launcher"));
		event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
			return new LauncherContainer(BeyRegistry.LAUNCHER_RIGHT_CONTAINER, windowId,
					new ItemStack(BeyRegistry.LEFTLAUNCHER), inv, Hand.MAIN_HAND);
		}).setRegistryName("left_launcher"));
		event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
			return new LauncherContainer(BeyRegistry.LAUNCHER_DUAL_CONTAINER, windowId,
					new ItemStack(BeyRegistry.DUALLAUNCHER), inv, Hand.MAIN_HAND);
		}).setRegistryName("dual_launcher"));
		if (!BeyRegistry.ITEMSLAYER.isEmpty() && BeyRegistry.ITEMSLAYERGT.size() != BeyRegistry.ITEMSLAYER.size()) {
			event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
				return new BeyContainer(BeyRegistry.BEY_CONTAINER, windowId,
						new ItemStack(BeyRegistry.ITEMSLAYER.get(0)), inv);
			}).setRegistryName("bey"));
		}
		if (!BeyRegistry.ITEMSLAYERGT.isEmpty()) {
			event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
				return new BeyGTContainer(BeyRegistry.BEY_GT_CONTAINER, windowId,
						new ItemStack(BeyRegistry.ITEMSLAYERGT.get(0)), inv, inv.player, Hand.MAIN_HAND);
			}).setRegistryName("beygt"));
		}
		if (!BeyRegistry.ITEMSLAYERGTNOWEIGHT.isEmpty()) {
			event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
				return new BeyGTNoWeightContainer(BeyRegistry.BEY_GT_CONTAINER_NO_WEIGHT, windowId,
						new ItemStack(BeyRegistry.ITEMSLAYERGTNOWEIGHT.get(0)), inv);
			}).setRegistryName("beygtnoweight"));
		}
		event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
			return new BeltContainer(BeyRegistry.BELT_CONTAINER, windowId, new ItemStack(BeyRegistry.ITEMS.get("belt")),
					inv, true);
		}).setRegistryName("belt"));
		event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
			return new BeyLoggerContainer(BeyRegistry.BEYLOGGER_CONTAINER, windowId);
		}).setRegistryName("beylogger"));
		event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
			return new BeyCreatorContainer(BeyRegistry.BEY_CREATOR_CONTAINER, windowId);
		}).setRegistryName("beycreator"));
		event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
			return new HandleContainer(BeyRegistry.HANDLE_CONTAINER, windowId, new ItemStack(BeyRegistry.LAUNCHER), inv,
					inv.player, Hand.MAIN_HAND);
		}).setRegistryName("handle"));
	}

	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
		for (Block block : BeyRegistry.BLOCKS) {
			event.getRegistry().register(block);
		}
	}

	@SubscribeEvent
	public static void enqueueIMC(final InterModEnqueueEvent event) {
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
				() -> SlotTypePreset.BELT.getMessageBuilder().build());
	}

	@SubscribeEvent
	public static void registerItem(final RegistryEvent.Register<Item> event) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			ClientEvents.injectResources();
		});

		event.getRegistry().register(BeyRegistry.LAYERICON);
		event.getRegistry().register(BeyRegistry.DISCICON);
		event.getRegistry().register(BeyRegistry.DRIVERICON);

		ItemCreator.getItemsFromFolder();
		BeyRegistry.ITEMS.forEach((name, item) -> {
			event.getRegistry().register(item);
		});
		event.getRegistry().register(new ItemBladerBelt("belt"));
		if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
			for (Item item : BeyRegistry.ITEMSLAYER) {
				event.getRegistry().register(item);
			}
		}
		if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
			for (Item item : BeyRegistry.ITEMSFRAME) {
				event.getRegistry().register(item);
			}
		}
		if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
			for (Item item : BeyRegistry.ITEMSDISCLIST) {
				event.getRegistry().register(item);
			}
		}
		if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
			for (Item item : BeyRegistry.ITEMSDRIVER) {
				event.getRegistry().register(item);
			}
		}
		if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
			for (Item item : BeyRegistry.ITEMSGTCHIP) {
				event.getRegistry().register(item);
			}
		}
		if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
			for (Item item : BeyRegistry.ITEMSGTWEIGHT) {
				event.getRegistry().register(item);
			}
		}
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class SpecialEvents {

		@SubscribeEvent
		public static void onCommandRegistry(final RegisterCommandsEvent event) {
			SetLevelCommand.register(event.getDispatcher());
			BeyCoinsCommand.register(event.getDispatcher());
			BeyBoxCommand.register(event.getDispatcher());
		}

		@SubscribeEvent
		public static void onPlayerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
			event.getPlayer().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
				PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getBladerLevel(), h.getExperience()),
						((ServerPlayerEntity) event.getPlayer()).connection.getNetworkManager(),
						NetworkDirection.PLAY_TO_CLIENT);
			});
		}

		@SubscribeEvent
		public static void playerClone(final PlayerEvent.Clone event) {
			if (event.isWasDeath()) {
				event.getOriginal().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
					event.getPlayer().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(i -> {
						i.setExperience(h.getExperience());
						i.setBladerLevel(h.getBladerLevel());
						i.setExpForNextLevel(h.getExpForNextLevel());
					});
				});
			}
		}

		@SubscribeEvent
		public static void playerCapabilitiesInjection(final AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof PlayerEntity) {
				event.addCapability(new ResourceLocation(Reference.MODID, "bladerlevel"), new BladerCapProvider());
			}
		}

		@SubscribeEvent
		public static void playerEnterWorld(final PlayerEvent.PlayerLoggedInEvent event) {
			StringTextComponent prefix = new StringTextComponent("[BeyCraft] -> Join to my Discord server: ");
			StringTextComponent url = new StringTextComponent("https://discord.gg/2PpbtFr");
			prefix.mergeStyle(TextFormatting.GOLD);
			url.mergeStyle(TextFormatting.GOLD);
			event.getPlayer().sendMessage(prefix, Util.DUMMY_UUID);
			event.getPlayer().sendMessage(url, Util.DUMMY_UUID);
			event.getPlayer().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
				PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getBladerLevel(), h.getExperience()),
						((ServerPlayerEntity) event.getPlayer()).connection.getNetworkManager(),
						NetworkDirection.PLAY_TO_CLIENT);
			});
		}
	}
}
