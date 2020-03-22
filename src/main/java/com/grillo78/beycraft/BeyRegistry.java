package com.grillo78.beycraft;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.grillo78.beycraft.blocks.ExpositoryBlock;
import com.grillo78.beycraft.blocks.StadiumBlock;
import com.grillo78.beycraft.capabilities.BladerLevelProvider;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.inventory.*;
import com.grillo78.beycraft.items.ItemBeyDisk;
import com.grillo78.beycraft.items.ItemBeyDiskFrame;
import com.grillo78.beycraft.items.ItemBeyFrame;
import com.grillo78.beycraft.items.ItemBeyLogger;
import com.grillo78.beycraft.items.ItemBeyPackage;
import com.grillo78.beycraft.items.ItemLauncher;
import com.grillo78.beycraft.items.ItemLauncherHandle;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;

public class BeyRegistry {
    public static List<Block> BLOCKS = Lists.newArrayList();
    public static HashMap<String,Item> ITEMS = new HashMap();
    public static List<Item> ITEMSLAYER = Lists.newArrayList();
    public static List<Item> ITEMSFRAMELIST = Lists.newArrayList();
    public static List<ItemBeyDiskFrame> ITEMSDISKFRAME = Lists.newArrayList();
    public static HashMap<String, ItemBeyFrame> ITEMSFRAME = new HashMap<String, ItemBeyFrame>();
    public static HashMap<String, ItemBeyDisk> ITEMSDISK = new HashMap<String, ItemBeyDisk>();
    public static final List<Item> ITEMSDISKLIST = Lists.newArrayList();
    public static List<Item> ITEMSDRIVER = Lists.newArrayList();

    /* Entity */
    public static final DeferredRegister<EntityType<?>> ENTITY = new DeferredRegister<>(ForgeRegistries.ENTITIES,
            Reference.MODID);
    public static final RegistryObject<EntityType<EntityBey>> BEY_ENTITY_TYPE = ENTITY.register("bey",
            () -> EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC)
                    .build(Reference.MODID + ":bey"));

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
    public static final ContainerType<BeyDiskFrameContainer> DISK_FRAME_CONTAINER = null;

    /* Keybinds */
    public static final KeyBinding beltKey = new KeyBinding("key.beycraft.belt",66,"key.beycraft.category");

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


}
