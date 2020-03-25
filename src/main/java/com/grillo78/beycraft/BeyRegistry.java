package com.grillo78.beycraft;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.grillo78.beycraft.blocks.ExpositoryBlock;
import com.grillo78.beycraft.blocks.StadiumBlock;
import com.grillo78.beycraft.capabilities.BladerLevelProvider;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.inventory.*;
import com.grillo78.beycraft.items.*;

import com.grillo78.beycraft.particles.SparkleParticle;
import com.grillo78.beycraft.proxy.ClientProxy;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;
import com.grillo78.beycraft.util.ItemCreator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
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
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeyRegistry {
    public static List<Block> BLOCKS = Lists.newArrayList();
    public static HashMap<String,Item> ITEMS = new HashMap();
    public static List<Item> ITEMSLAYER = Lists.newArrayList();
    public static List<Item> ITEMSFRAMELIST = Lists.newArrayList();
    public static List<ItemBeyDiscFrame> ITEMSDISCFRAME = Lists.newArrayList();
    public static HashMap<String, ItemBeyFrame> ITEMSFRAME = new HashMap<String, ItemBeyFrame>();
    public static HashMap<String, ItemBeyDisc> ITEMSDISC = new HashMap<String, ItemBeyDisc>();
    public static final List<Item> ITEMSDISCLIST = Lists.newArrayList();
    public static List<Item> ITEMSDRIVER = Lists.newArrayList();

    /* Entity */
    public static final DeferredRegister<EntityType<?>> ENTITY = new DeferredRegister<>(ForgeRegistries.ENTITIES,
            Reference.MODID);
    public static final RegistryObject<EntityType<EntityBey>> BEY_ENTITY_TYPE = ENTITY.register("bey",
            () -> EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC)
                    .build(Reference.MODID + ":bey"));


    /* TileEntity */
    @ObjectHolder(Reference.MODID + ":expositorytileentity")
    public static TileEntityType<ExpositoryTileEntity> EXPOSITORYTILEENTITYTYPE = null;

    /* Container */
    @ObjectHolder("beycraft:launcher")
    public static final ContainerType<LauncherContainer> LAUNCHER_CONTAINER = null;
    @ObjectHolder("beycraft:bey")
    public static final ContainerType<BeyContainer> BEY_CONTAINER = null;
    @ObjectHolder("beycraft:belt")
    public static final ContainerType<BeltContainer> BELT_CONTAINER = null;
    @ObjectHolder("beycraft:handle")
    public static final ContainerType<HandleContainer> HANDLE_CONTAINER = null;
    @ObjectHolder("beycraft:diskframe")
    public static final ContainerType<BeyDiscFrameContainer> DISK_FRAME_CONTAINER = null;


    /* ArmorMaterials */
//	public static final ArmorMaterial BLADER_MATERIAL = EnumHelper.addArmorMaterial("blader_model",
//			Reference.MODID + ":blader_clothes", 1000, new int[] { 10, 10, 10, 10 }, 0,
//			SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0F);

    /* Sounds */
    public static final SoundEvent HITSOUND = new SoundEvent(new ResourceLocation(Reference.MODID, "bey.hit"));

    /* Items */
    public static final ItemBeyPackage BEYPACKAGE = new ItemBeyPackage("package");

    public static final Item LAYERICON = new Item(new Item.Properties()).setRegistryName(Reference.MODID, "layericon");
    public static final Item DISKICON = new Item(new Item.Properties()).setRegistryName(Reference.MODID, "diskicon");
    public static final Item DRIVERICON = new Item(new Item.Properties()).setRegistryName(Reference.MODID, "drivericon");


    public static final ItemLauncher REDLAUNCHER = new ItemLauncher("red_launcher", 1);
    public static final ItemLauncher LEFTLAUNCHER = new ItemLauncher("left_launcher", -1);
    public static final ItemLauncherHandle LAUNCHERHANDLE = new ItemLauncherHandle("launcherhandle");
    public static final ItemBeyLogger BEYLOGGER = new ItemBeyLogger("beylogger");
    public static final ItemBeyLogger BEYLOGGERPLUS = new ItemBeyLogger("beylogger_plus");

    /**
     * Armors
     */
//	public static final ItemBladerArmor AIGER_CHESTPLATE = new ItemBladerArmor(BLADER_MATERIAL, "Aiger_chestplate",
//			EntityEquipmentSlot.CHEST, "Aiger");
//	public static final ItemBladerArmor AIGER_LEGGINGS = new ItemBladerArmor(BLADER_MATERIAL, "Aiger_leggings",
//			EntityEquipmentSlot.LEGS, "Aiger");
//	public static final ItemBladerArmor AIGER_BOOTS = new ItemBladerArmor(BLADER_MATERIAL, "Aiger_boots",
//			EntityEquipmentSlot.FEET, "Aiger");
//	public static final ItemBladerArmor VALT_CHESTPLATE = new ItemBladerArmor(BLADER_MATERIAL, "valt_chestplate",
//			EntityEquipmentSlot.CHEST, "Aiger");
//	public static final ItemBladerArmor VALT_LEGGINGS = new ItemBladerArmor(BLADER_MATERIAL, "valt_leggings",
//			EntityEquipmentSlot.LEGS, "Aiger");
//	public static final ItemBladerArmor VALT_BOOTS = new ItemBladerArmor(BLADER_MATERIAL, "valt_boots",
//			EntityEquipmentSlot.FEET, "Aiger");
//	public static final ItemBladerArmor TURBO_VALT_CHESTPLATE = new ItemBladerArmor(BLADER_MATERIAL,
//			"turbo_valt_chestplate", EntityEquipmentSlot.CHEST, "Aiger");
//	public static final ItemBladerArmor TURBO_VALT_LEGGINGS = new ItemBladerArmor(BLADER_MATERIAL,
//			"turbo_valt_leggings", EntityEquipmentSlot.LEGS, "Aiger");
//	public static final ItemBladerArmor TURBO_VALT_BOOTS = new ItemBladerArmor(BLADER_MATERIAL, "turbo_valt_boots",
//			EntityEquipmentSlot.FEET, "Aiger");

    /* Blocks */
    public static final ExpositoryBlock EXPOSITORY = new ExpositoryBlock(Material.ANVIL, "expository");
    public static final StadiumBlock STADIUM = new StadiumBlock(Material.IRON, "stadium");

    /* Profession */
//    @Nullable
//    public static VillagerProfession VILLAGERBEYTRADER = new VillagerProfession();



    @SubscribeEvent
    public static void playerCapabilitiesInjection(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Reference.MODID, "BladerLevel"), new BladerLevelProvider());
        }
    }

//	@SubscribeEvent
//	public static void playerCapabilitiesInjection(AttachCapabilitiesEvent<Entity> event) {
//		if (event.getObject() instanceof PlayerEntity) {
//			event.addCapability(new ResourceLocation(Reference.MODID, "BladerLevel"), new Provider());
//		}
//	}

//	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
//		float f = 0.00390625F;
//		float f1 = 0.00390625F;
//		Tessellator tessellator = Tessellator.getInstance();
//		BufferBuilder buffer = tessellator.getBuffer();
//		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//		buffer.pos((double) (x + 0), (double) (y + height), 0.0D)
//				.tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
//		;
//		buffer.pos((double) (x + width), (double) (y + height), 0.0D)
//				.tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1))
//				.endVertex();
//		buffer.pos((double) (x + width), (double) (y + 0), 0.0D)
//				.tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
//		buffer.pos((double) (x + 0), (double) (y + 0), 0.0D)
//				.tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
//		tessellator.draw();
//	}


    @SubscribeEvent
    public void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {

        }
    }

    @ObjectHolder(Reference.MODID + ":sparkle")
    public static BasicParticleType SPARKLE;

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
    public static void onParticleFactorieRegistry(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(SPARKLE, SparkleParticle.Factory::new);
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
        ItemCreator.getItemsFromFolder();
        BeyRegistry.ITEMS.forEach((name, item) -> {
            event.getRegistry().register(item);
        });
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ClientProxy.injectResources();
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

    @SubscribeEvent
    public static void editHud(RenderGameOverlayEvent.Post event) {
        if (!Minecraft.getInstance().gameSettings.showDebugInfo) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                Minecraft.getInstance().getRenderManager().textureManager
                        .bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/bladerlevel.png"));
            }
        }
    }

}
