package com.grillo78.BeyCraft;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.grillo78.BeyCraft.blocks.ExpositoryBlock;
import com.grillo78.BeyCraft.blocks.StadiumBlock;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.inventory.BeyContainer;
import com.grillo78.BeyCraft.inventory.BeyDiskFrameContainer;
import com.grillo78.BeyCraft.inventory.HandleContainer;
import com.grillo78.BeyCraft.inventory.LauncherContainer;
import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDiskFrame;
import com.grillo78.BeyCraft.items.ItemBeyFrame;
import com.grillo78.BeyCraft.items.ItemBeyLogger;
import com.grillo78.BeyCraft.items.ItemBeyPackage;
import com.grillo78.BeyCraft.items.ItemLauncher;
import com.grillo78.BeyCraft.items.ItemLauncherHandle;
import com.grillo78.BeyCraft.tileentity.ExpositoryTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class BeyRegistry {
	public static List<Block> BLOCKS = Lists.newArrayList();
	public static List<Item> ITEMS = Lists.newArrayList();
	public static List<Item> ITEMSLAYER = Lists.newArrayList();
	public static List<Item> ITEMSFRAMELIST = Lists.newArrayList();
	public static List<ItemBeyDiskFrame> ITEMSDISKFRAME = Lists.newArrayList();
	public static HashMap<String,ItemBeyFrame> ITEMSFRAME = new HashMap<String,ItemBeyFrame>();
	public static HashMap<String,ItemBeyDisk> ITEMSDISK = new HashMap<String,ItemBeyDisk>();
	public static final List<Item> ITEMSDISKLIST = Lists.newArrayList();
	public static List<Item> ITEMSDRIVER = Lists.newArrayList();

	/* Entity */
	public static final DeferredRegister<EntityType<?>> ENTITY = new DeferredRegister<>(ForgeRegistries.ENTITIES,
			Reference.MODID);
	public static final RegistryObject<EntityType<EntityBey>> BEY_ENTITY_TYPE = ENTITY.register("bey",
			() -> EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC)
					.build(Reference.MODID + ":bey"));

	/* TileEntity */
	public static final DeferredRegister<TileEntityType<?>> EXPOSITORY_TILE_ENTITY = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES,
			Reference.MODID);
	public static RegistryObject<TileEntityType<ExpositoryTileEntity>> EXPOSITORY_TILE_ENTITY_TYPE = EXPOSITORY_TILE_ENTITY
			.register("expositorytileentity",
			() -> TileEntityType.Builder.<ExpositoryTileEntity>create(ExpositoryTileEntity::new, BeyRegistry.EXPOSITORY).build(null));

	/* Container */
	@ObjectHolder("beycraft:launcher")
	public static final ContainerType<LauncherContainer> LAUNCHER_CONTAINER=null;
	@ObjectHolder("beycraft:bey")
	public static final ContainerType<BeyContainer> BEY_CONTAINER=null;
	@ObjectHolder("beycraft:handle")
	public static final ContainerType<HandleContainer> HANDLE_CONTAINER=null;
	@ObjectHolder("beycraft:diskframe")
	public static final ContainerType<BeyDiskFrameContainer> DISK_FRAME_CONTAINER=null;

	/* ArmorMaterials */
//	public static final ArmorMaterial BLADER_MATERIAL = EnumHelper.addArmorMaterial("blader_model",
//			Reference.MODID + ":blader_clothes", 1000, new int[] { 10, 10, 10, 10 }, 0,
//			SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0F);

	/* Items */
	public static final ItemBeyPackage BEYPACKAGE = new ItemBeyPackage("package");
	
	
	public static final Item LAYERICON = new Item(new Item.Properties()).setRegistryName(Reference.MODID, "layericon");
	public static final Item DISKICON = new Item(new Item.Properties()).setRegistryName(Reference.MODID, "diskicon");
	public static final Item DRIVERICON = new Item(new Item.Properties()).setRegistryName(Reference.MODID, "drivericon");
	
//	public static final ItemBeyLayer FAFNIRF3 = new ItemBeyLayer("drain_fafnir", -0.08F, -1, 1, 3, 5, 1, new Absorb(),
//			null, BeyTypes.STAMINA);
//	public static final ItemBeyDisk EIGHTDISK = new ItemBeyDisk("8disk", -0.15F);
//	public static final ItemBeyDriver NOPTHINGDRIVER = new ItemBeyDriver("nothing_driver", 0.15F, 1, 3, null, null,
//			BeyTypes.STAMINA);
//	public static final ItemBeyLayer FAFNIRF4 = new ItemBeyLayer("geist_fafnir", -0.08F, -1, 1, 3, 5, 1, new Absorb(),
//			null, BeyTypes.STAMINA);
//	public static final ItemBeyLayer FAFNIRF5 = new ItemBeyLayer("wizard_fafnir", -0.08F, -1, 1, 3, 5, 1, new Absorb(),
//			null, BeyTypes.STAMINA);
//	public static final ItemBeyDisk RATCHETDISK = new ItemBeyDisk("ratchet", -0.15F);
//	public static final ItemBeyDriver RISEDRIVER = new ItemBeyDriver("rise_driver", 0.15F, 1, 3, null, null,
//			BeyTypes.STAMINA);
//	public static final ItemBeyLayer VALTRYEKV4 = new ItemBeyLayer("valtryekv4", -0.13F, 1, 5, 0, 5, 5, null, null,
//			BeyTypes.ATTACK);
//	public static final ItemBeyDisk TWELVEDISK = new ItemBeyDisk("12disk", -0.15F);
//	public static final ItemBeyDriver VOLCANICDRIVER = new ItemBeyDriver("volcanic_driver", 0.15F, 1, 1, null, null,
//			BeyTypes.ATTACK);
//	public static final ItemBeyLayer VALTRYEKV2 = new ItemBeyLayer("valtryekv2", -0.08F, 1, 4, 0, 1, 4, null, null,
//			BeyTypes.ATTACK);
//	public static final ItemBeyDisk BOOSTDISK = new ItemBeyDisk("boostdisk", -0.15F);
//	public static final ItemBeyDriver VARIABLEDRIVER = new ItemBeyDriver("variable_driver", 0.15F, 1, 1, null, null,
//			BeyTypes.ATTACK);
//	public static final ItemBeyLayer VALTRYEKV5 = new ItemBeyLayer("valtryekv5", -0.15F, 1, 7, 0, 5, 5, null, null,
//			BeyTypes.ATTACK);
//	public static final ItemBeyDisk ZENITHDISK = new ItemBeyDisk("zenithdisk", -0.15F);
//	public static final ItemBeyDriver EVOLUTIONDRIVER = new ItemBeyDriver("evolution_driver", 0.15F, 1, 1, null, null,
//			BeyTypes.ATTACK);
//	public static final ItemBeyLayer REQUIEMSPRYZEN = new ItemBeyLayerDual("requiem_spryzen", -0.23F, 3, 3, 4, 1,
//			new Absorb(), null, BeyTypes.BALANCE);
//	public static final ItemBeyDisk ZERODISK = new ItemBeyDisk("0disk", -0.15F);
//	public static final ItemBeyLayer TURBOSPRYZEN = new ItemBeyLayerDual("turbospryzen", -0.12F, 5, 3, 5, 2, null, null,
//			BeyTypes.BALANCE);
//	public static final ItemBeyDisk ZEROWDISK = new ItemBeyDisk("0wdisk", -0.15F);
//	public static final ItemBeyDriver ZETASDRIVER = new ItemBeyDriver("zetas_driver", 0.15F, 1, 1,
//			new MultiType(Arrays.asList(new BeyTypes[] { BeyTypes.ATTACK, BeyTypes.DEFENSE, BeyTypes.STAMINA })), null,
//			BeyTypes.ATTACK);
//	public static final ItemBeyLayer SPRYZEN = new ItemBeyLayer("spryzen", -0.23F, 1, 5, 3, 5, 2, null, null,
//			BeyTypes.BALANCE);
//	public static final ItemBeyDisk SPREADDISK = new ItemBeyDisk("spreaddisk", -0.15F);
//	public static final ItemBeyDriver FUSIONDRIVER = new ItemBeyDriver("fusion_driver", 0.15F, 1, 1, null, null,
//			BeyTypes.BALANCE);
//	public static final ItemBeyLayer STORMSPRYZEN = new ItemBeyLayer("storm_spryzen", -0.23F, 1, 5, 3, 5, 2, null, null,
//			BeyTypes.BALANCE);
//	public static final ItemBeyDisk KNUKLEDISK = new ItemBeyDisk("knukledisk", -0.15F);
//	public static final ItemBeyDriver UNITEDRIVER = new ItemBeyDriver("unite_driver", 0.15F, 1, 1, null, null,
//			BeyTypes.BALANCE);
//	public static final ItemBeyLayer SALAMANDERS4 = new ItemBeyLayer("salamanders4", -0.08F, -1, 2, 3, 5, 2,
//			new MultiType(Arrays.asList(new BeyTypes[] { BeyTypes.ATTACK, BeyTypes.DEFENSE })), null, BeyTypes.ATTACK);
//	public static final ItemBeyDriver OPERATEDRIVER = new ItemBeyDriver("operate_driver", 0.15F, 1, 1,
//			new MultiType(Arrays.asList(new BeyTypes[] { BeyTypes.ATTACK, BeyTypes.DEFENSE })), null, BeyTypes.ATTACK);
//	public static final ItemBeyLayer VALTRYEKV3 = new ItemBeyLayer("valtryekv3", -0.08F, 1, 4, 0, 1, 5, null, null,
//			BeyTypes.ATTACK);
//	public static final ItemBeyLayer STRIKEVALTRYEKV3 = new ItemBeyLayer("strike_valtryek", -0.08F, 1, 4, 0, 1, 5, null,
//			null, BeyTypes.ATTACK);
//	public static final ItemBeyDisk SIXVDISK = new ItemBeyDisk("6vdisk", -0.15F);
//	public static final ItemBeyDriver REBOOTDRIVER = new ItemBeyDriver("reboot_driver", 0.15F, 1, 1,
//			new MultiType(Arrays.asList(new BeyTypes[] { BeyTypes.ATTACK, BeyTypes.DEFENSE })), null, BeyTypes.ATTACK);
	
	public static final ItemLauncher REDLAUNCHER = new ItemLauncher("red_launcher", 1);
	public static final ItemLauncher LEFTLAUNCHER = new ItemLauncher("left_launcher", -1);
	public static final ItemLauncherHandle LAUNCHERHANDLE = new ItemLauncherHandle("launcherhandle");
	public static final ItemBeyLogger BEYLOGGER = new ItemBeyLogger("beylogger");
	public static final ItemBeyLogger BEYLOGGERPLUS = new ItemBeyLogger("beylogger_plus");

	/** Armors */
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

//	@SubscribeEvent
//	public static void registerBey(RegistryEvent.Register<EntityEntry> event) {
//		event.getRegistry().register(BEYENTITY);
//	}

//	@SubscribeEvent
//	public static void onModelRegister(final ModelRegistryEvent event) {
//		for (Item item : ITEMS) {
//		}
//		for (Item item : ITEMSLAYER) {
//		}
//		for (Item item : ITEMSDISK) {
//		}
//		for (Item item : ITEMSDRIVER) {
//		}
//		for (Block block : BLOCKS) {
//		}
//	}

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

	
	
}
