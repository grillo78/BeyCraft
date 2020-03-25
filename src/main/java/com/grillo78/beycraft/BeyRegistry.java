package com.grillo78.beycraft;

import com.google.common.collect.Lists;
import com.grillo78.beycraft.blocks.ExpositoryBlock;
import com.grillo78.beycraft.blocks.StadiumBlock;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.inventory.*;
import com.grillo78.beycraft.items.*;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashMap;
import java.util.List;

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

    @ObjectHolder(Reference.MODID + ":sparkle")
    public static BasicParticleType SPARKLE;

    /* Entity */
    public static final EntityType<EntityBey> BEY_ENTITY_TYPE = EntityType.Builder.<EntityBey>create(EntityBey::new, EntityClassification.MISC)
            .build(Reference.MODID + ":bey");


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

}
