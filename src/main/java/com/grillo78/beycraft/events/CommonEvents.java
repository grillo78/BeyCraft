package com.grillo78.beycraft.events;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.capabilities.BladerLevelProvider;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.inventory.*;
import com.grillo78.beycraft.items.ItemBladerBelt;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;
import com.grillo78.beycraft.util.ItemCreator;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {
    @SubscribeEvent
    public static void playerCapabilitiesInjection(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Reference.MODID, "BladerLevel"), new BladerLevelProvider());
        }
    }

    @SubscribeEvent
    public void registerEntityType(final RegistryEvent.Register<EntityType<?>> event) {
        EntityType<?> type = EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC)
                .size(0.19F, 0.25F).build(Reference.MODID + ":bey");
        type.setRegistryName(Reference.MODID, "bey");
        event.getRegistry().register(type);
    }

    @SubscribeEvent
    public void registerTileEntityType(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register((TileEntityType<ExpositoryTileEntity>) TileEntityType.Builder
                .create(ExpositoryTileEntity::new, BeyRegistry.EXPOSITORY)
                .build(null)
                .setRegistryName(new ResourceLocation(Reference.MODID, "expositorytileentity")));
    }

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
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            return new BeyDiscFrameContainer(windowId, new ItemStack(BeyRegistry.REDLAUNCHER), inv, inv.player, Hand.MAIN_HAND);
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
        DistExecutor.runWhenOn(Dist.CLIENT,()->()->{
            ClientEvents.injectResources();
        });

        ItemCreator.getItemsFromFolder();
        BeyRegistry.ITEMS.forEach((name, item) -> {
            event.getRegistry().register(item);
        });
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
        for (Item item : BeyRegistry.ITEMSDISCLIST) {
            event.getRegistry().register(item);
        }
        for (Item item : BeyRegistry.ITEMSDRIVER) {
            event.getRegistry().register(item);
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

}
