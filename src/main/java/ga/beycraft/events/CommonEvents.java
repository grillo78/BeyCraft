package ga.beycraft.events;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.commands.BeyCoinsCommand;
import ga.beycraft.commands.GetBeyCoinsCommand;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.inventory.*;
import ga.beycraft.inventory.slots.BeyLoggerContainer;
import ga.beycraft.items.ItemBladerBelt;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageGetExperience;
import ga.beycraft.network.message.MessageSyncBladerLevel;
import ga.beycraft.tileentity.*;
import ga.beycraft.util.ItemCreator;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {

    @SubscribeEvent
    public static void registerEntityType(final RegistryEvent.Register<EntityType<?>> event) {
        EntityType<?> type = EntityType.Builder.<EntityBey>of(EntityBey::new, EntityClassification.MISC)
                .noSummon().sized(0.19F, 0.25F).build(Reference.MOD_ID + ":bey");
        type.setRegistryName(Reference.MOD_ID, "bey");
        event.getRegistry().register(type);

    }

    @SubscribeEvent
    public static void registerEntityAttributes(final EntityAttributeCreationEvent event) {
        event.put(BeyCraftRegistry.BEY_ENTITY_TYPE, EntityBey.registerAttributes().build());
    }

    @SubscribeEvent
    public static void registerTileEntityType(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.of(ExpositoryTileEntity::new, BeyCraftRegistry.EXPOSITORY)
                .build(null).setRegistryName(new ResourceLocation(Reference.MOD_ID, "expositorytileentity")));
        event.getRegistry()
                .register(TileEntityType.Builder.of(BeyCreatorTileEntity::new, BeyCraftRegistry.BEYCREATORBLOCK)
                        .build(null).setRegistryName(new ResourceLocation(Reference.MOD_ID, "beycreatortileentity")));
        event.getRegistry().register(TileEntityType.Builder.of(RobotTileEntity::new, BeyCraftRegistry.ROBOT).build(null)
                .setRegistryName(new ResourceLocation(Reference.MOD_ID, "robottileentity")));
        event.getRegistry().register(TileEntityType.Builder.of(StadiumTileEntity::new, BeyCraftRegistry.STADIUM)
                .build(null).setRegistryName(new ResourceLocation(Reference.MOD_ID, "stadiumtileentity")));
        event.getRegistry().register(TileEntityType.Builder.of(BattleInformerTileEntity::new, BeyCraftRegistry.BATTLE_INFORMER)
                .build(null).setRegistryName(new ResourceLocation(Reference.MOD_ID, "battleinformer")));
    }

    @SubscribeEvent
    public static void onParticleTypeRegistry(final RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().register(new BasicParticleType(false).setRegistryName("sparkle"));
        event.getRegistry().register(new BasicParticleType(false).setRegistryName("resonance_bey"));
        event.getRegistry().register(new BasicParticleType(false).setRegistryName("resonance_player"));
    }

    @SubscribeEvent
    public static void onSoundRegistry(final RegistryEvent.Register<SoundEvent> event) {
        BeyCraftRegistry.HITSOUND.setRegistryName("bey.hit");
        event.getRegistry().register(BeyCraftRegistry.HITSOUND);
        BeyCraftRegistry.OPEN_CLOSE_BELT.setRegistryName("open_close_belt");
        event.getRegistry().register(BeyCraftRegistry.OPEN_CLOSE_BELT);
        BeyCraftRegistry.COUNTDOWN.setRegistryName("countdown");
        event.getRegistry().register(BeyCraftRegistry.COUNTDOWN);
    }

    @SubscribeEvent
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        if (!BeyCraftRegistry.ITEMSDISCFRAME.isEmpty()) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new BeyDiscFrameContainer(windowId, new ItemStack(BeyCraftRegistry.LAUNCHER), inv);
            }).setRegistryName("discframe"));
        }
        if (!BeyCraftRegistry.ITEMSLAYERGOD.isEmpty()) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new BeyGodContainer(BeyCraftRegistry.BEY_GOD_CONTAINER, windowId, new ItemStack(BeyCraftRegistry.ITEMSLAYERGOD.get(0)), inv);
            }).setRegistryName("beygod"));
        }
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            return new LauncherContainer(BeyCraftRegistry.LAUNCHER_RIGHT_CONTAINER, windowId,
                    new ItemStack(BeyCraftRegistry.LAUNCHER), inv, Hand.MAIN_HAND);
        }).setRegistryName("right_launcher"));
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            return new LauncherContainer(BeyCraftRegistry.LAUNCHER_RIGHT_CONTAINER, windowId,
                    new ItemStack(BeyCraftRegistry.LEFTLAUNCHER), inv, Hand.MAIN_HAND);
        }).setRegistryName("left_launcher"));
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            return new LauncherContainer(BeyCraftRegistry.LAUNCHER_DUAL_CONTAINER, windowId,
                    new ItemStack(BeyCraftRegistry.DUALLAUNCHER), inv, Hand.MAIN_HAND);
        }).setRegistryName("dual_launcher"));
        if (!BeyCraftRegistry.ITEMSLAYER.isEmpty() && BeyCraftRegistry.ITEMSLAYERGT.size() != BeyCraftRegistry.ITEMSLAYER.size()) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new BeyContainer(BeyCraftRegistry.BEY_CONTAINER, windowId,
                        new ItemStack(BeyCraftRegistry.ITEMSLAYER.get(0)), inv);
            }).setRegistryName("bey"));
        }
        if (!BeyCraftRegistry.ITEMSLAYERGT.isEmpty()) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new BeyGTContainer(BeyCraftRegistry.BEY_GT_CONTAINER, windowId,
                        new ItemStack(BeyCraftRegistry.ITEMSLAYERGT.get(0)), inv, inv.player, Hand.MAIN_HAND);
            }).setRegistryName("beygt"));
        }
        if (!BeyCraftRegistry.ITEMSLAYERGTNOWEIGHT.isEmpty()) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                return new BeyGTNoWeightContainer(BeyCraftRegistry.BEY_GT_CONTAINER_NO_WEIGHT, windowId,
                        new ItemStack(BeyCraftRegistry.ITEMSLAYERGTNOWEIGHT.get(0)), inv);
            }).setRegistryName("beygtnoweight"));
        }
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            return new BeltContainer(BeyCraftRegistry.BELT_CONTAINER, windowId, new ItemStack(BeyCraftRegistry.ITEMS.get("belt")),
                    inv, true);
        }).setRegistryName("belt"));
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            return new BeyLoggerContainer(BeyCraftRegistry.BEYLOGGER_CONTAINER, windowId);
        }).setRegistryName("beylogger"));
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            return new HandleContainer(BeyCraftRegistry.HANDLE_CONTAINER, windowId, new ItemStack(BeyCraftRegistry.LAUNCHER), inv,
                    inv.player, Hand.MAIN_HAND);
        }).setRegistryName("handle"));
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        for (Block block : BeyCraftRegistry.BLOCKS) {
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event) {

        event.getRegistry().register(BeyCraftRegistry.LAYERICON);
        event.getRegistry().register(BeyCraftRegistry.DISCICON);
        event.getRegistry().register(BeyCraftRegistry.DRIVERICON);

        ItemCreator.getItemsFromFolder();
        BeyCraftRegistry.ITEMS.forEach((name, item) -> {
            event.getRegistry().register(item);
        });
        event.getRegistry().register(new ItemBladerBelt("belt"));
        if (!BeyCraftRegistry.ITEMSLAYER.isEmpty()) {
            for (Item item : BeyCraftRegistry.ITEMSLAYER) {
                event.getRegistry().register(item);
            }
        }
        if (!BeyCraftRegistry.ITEMSFRAME.isEmpty()) {
            for (Item item : BeyCraftRegistry.ITEMSFRAME) {
                event.getRegistry().register(item);
            }
        }
        if (!BeyCraftRegistry.ITEMSDISCLIST.isEmpty()) {
            for (Item item : BeyCraftRegistry.ITEMSDISCLIST) {
                event.getRegistry().register(item);
            }
        }
        if (!BeyCraftRegistry.ITEMSDRIVER.isEmpty()) {
            for (Item item : BeyCraftRegistry.ITEMSDRIVER) {
                event.getRegistry().register(item);
            }
        }
        if (!BeyCraftRegistry.ITEMSGTCHIP.isEmpty()) {
            for (Item item : BeyCraftRegistry.ITEMSGTCHIP) {
                event.getRegistry().register(item);
            }
        }
        if (!BeyCraftRegistry.ITEMSGTWEIGHT.isEmpty()) {
            for (Item item : BeyCraftRegistry.ITEMSGTWEIGHT) {
                event.getRegistry().register(item);
            }
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class SpecialEvents {

        @SubscribeEvent
        public static void onCommandRegistry(final RegisterCommandsEvent event) {
            BeyCoinsCommand.register(event.getDispatcher());
            GetBeyCoinsCommand.register(event.getDispatcher());
        }

        @SubscribeEvent
        public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
            if (event.player.isAlive())
                event.player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(bladerLevel -> {
					bladerLevel.tick();
                });
        }

        @SubscribeEvent
        public static void onPlayerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
            event.getPlayer().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
                PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getExperience(), h.isInResonance(), true, event.getPlayer().getId()),
                        ((ServerPlayerEntity) event.getPlayer()).connection.getConnection(),
                        NetworkDirection.PLAY_TO_CLIENT);
            });
        }

        @SubscribeEvent
        public static void playerClone(final PlayerEvent.Clone event) {
            if (event.isWasDeath()) {
                event.getOriginal().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
                    event.getPlayer().getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(i -> {
                        i.setExperience(h.getExperience());
                        i.setExpForNextLevel(h.getExpForNextLevel());
                    });
                });
            }
        }

        @SubscribeEvent
        public static void playerCapabilitiesInjection(final AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof PlayerEntity) {
                event.addCapability(new ResourceLocation(Reference.MOD_ID, "bladerlevel"), new BladerCapProvider());
            }
        }

        @SubscribeEvent
        public static void playerEnterWorld(final PlayerEvent.PlayerLoggedInEvent event) {
            StringTextComponent prefix = new StringTextComponent("[BeyCraft] -> Join to my Discord server: ");
            StringTextComponent url = new StringTextComponent("https://discord.gg/2PpbtFr");
            prefix.withStyle(TextFormatting.GOLD);
            url.withStyle(TextFormatting.GOLD);
            event.getPlayer().sendMessage(prefix, Util.NIL_UUID);
            event.getPlayer().sendMessage(url, Util.NIL_UUID);


            PacketHandler.instance.sendTo(new MessageGetExperience(),
                    ((ServerPlayerEntity) event.getPlayer()).connection.getConnection(),
                    NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
