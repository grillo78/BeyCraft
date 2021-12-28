package ga.beycraft;

import com.google.common.collect.Lists;
import ga.beycraft.blocks.*;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.inventory.*;
import ga.beycraft.inventory.slots.BeyLoggerContainer;
import ga.beycraft.items.*;
import ga.beycraft.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ObjectHolder;
import xyz.heroesunited.heroesunited.common.objects.container.EquipmentAccessoriesSlot;

import java.util.HashMap;
import java.util.List;

public class BeyCraftRegistry {
    public static List<Block> BLOCKS = Lists.newArrayList();
    public static HashMap<String,Item> ITEMS = new HashMap();
    public static List<Item> ITEMSLAYER = Lists.newArrayList();
    public static List<Item> ITEMSLAYERGOD = Lists.newArrayList();
    public static List<Item> ITEMSLAYERGT = Lists.newArrayList();
    public static List<Item> ITEMSLAYERGTNOWEIGHT = Lists.newArrayList();
    public static List<Item> ITEMSFRAME = Lists.newArrayList();
    public static List<Item> ITEMSGTWEIGHT = Lists.newArrayList();
    public static List<Item> ITEMSDISCFRAME = Lists.newArrayList();
    public static final List<Item> ITEMSDISCLIST = Lists.newArrayList();
    public static List<Item> ITEMSDRIVER = Lists.newArrayList();
    public static List<Item> ITEMSGTCHIP = Lists.newArrayList();
    public static List<Item> ITEMSGODCHIP = Lists.newArrayList();

    @ObjectHolder(Reference.MOD_ID + ":sparkle")
    public static BasicParticleType SPARKLE;
    @ObjectHolder(Reference.MOD_ID + ":aura")
    public static BasicParticleType AURA;

    /* Entity */
    @ObjectHolder(Reference.MOD_ID + ":bey")
    public static final EntityType<EntityBey> BEY_ENTITY_TYPE = null;


    /* TileEntity */
    @ObjectHolder(Reference.MOD_ID + ":expositorytileentity")
    public static final TileEntityType<ExpositoryTileEntity> EXPOSITORYTILEENTITYTYPE = null;
    @ObjectHolder(Reference.MOD_ID + ":beycreatortileentity")
    public static final TileEntityType<BeyCreatorTileEntity> BEYCREATORTILEENTITYTYPE = null;
    @ObjectHolder(Reference.MOD_ID + ":robottileentity")
    public static final TileEntityType<RobotTileEntity> ROBOTTILEENTITYTYPE = null;
    @ObjectHolder(Reference.MOD_ID + ":stadiumtileentity")
    public static final TileEntityType<StadiumTileEntity> STADIUMTILEENTITYTYPE = null;
    @ObjectHolder(Reference.MOD_ID + ":battleinformer")
    public static final TileEntityType<BattleInformerTileEntity> BATTLE_INFORMER_TILE_ENTITY_TYPE = null;

    /* Container */
    @ObjectHolder("beycraft:right_launcher")
    public static final ContainerType<LauncherContainer> LAUNCHER_RIGHT_CONTAINER = null;
    @ObjectHolder("beycraft:left_launcher")
    public static final ContainerType<LauncherContainer> LAUNCHER_LEFT_CONTAINER = null;
    @ObjectHolder("beycraft:dual_launcher")
    public static final ContainerType<LauncherContainer> LAUNCHER_DUAL_CONTAINER = null;
    @ObjectHolder("beycraft:bey")
    public static final ContainerType<BeyContainer> BEY_CONTAINER = null;
    @ObjectHolder("beycraft:beygt")
    public static final ContainerType<BeyGTContainer> BEY_GT_CONTAINER = null;
    @ObjectHolder("beycraft:beygtnoweight")
    public static final ContainerType<BeyGTNoWeightContainer> BEY_GT_CONTAINER_NO_WEIGHT = null;
    @ObjectHolder("beycraft:beygod")
    public static final ContainerType<BeyGodContainer> BEY_GOD_CONTAINER = null;
    @ObjectHolder("beycraft:belt")
    public static final ContainerType<BeltContainer> BELT_CONTAINER = null;
    @ObjectHolder("beycraft:handle")
    public static final ContainerType<HandleContainer> HANDLE_CONTAINER = null;
    @ObjectHolder("beycraft:discframe")
    public static final ContainerType<BeyDiscFrameContainer> DISC_FRAME_CONTAINER = null;
    @ObjectHolder("beycraft:beylogger")
    public static final ContainerType<BeyLoggerContainer> BEYLOGGER_CONTAINER = null;


    /* ArmorMaterials */
//	public static final ArmorMaterial BLADER_MATERIAL = EnumHelper.addArmorMaterial("blader_model",
//			Reference.MODID + ":blader_clothes", 1000, new int[] { 10, 10, 10, 10 }, 0,
//			SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0F);

    /* Sounds */
    public static final SoundEvent HITSOUND = new SoundEvent(new ResourceLocation(Reference.MOD_ID, "bey.hit"));
    public static final SoundEvent OPEN_CLOSE_BELT = new SoundEvent(new ResourceLocation(Reference.MOD_ID, "open_close_belt"));
    public static final SoundEvent COUNTDOWN = new SoundEvent(new ResourceLocation(Reference.MOD_ID, "countdown"));

    /* Items */
    public static final ItemBeyPackage BEYPACKAGE = new ItemBeyPackage("package");
    public static final ItemBeyCoin BEYCOIN = new ItemBeyCoin("beycoin");
    public static final ItemBeyRubber BEYRUBBER = new ItemBeyRubber("rubber");
    public static final ItemPlastic PLASTIC = new ItemPlastic("plastic");

    public static final Item LAYERICON = new Item(new Item.Properties()).setRegistryName(Reference.MOD_ID, "layericon");
    public static final Item DISCICON = new Item(new Item.Properties()).setRegistryName(Reference.MOD_ID, "discicon");
    public static final Item DRIVERICON = new Item(new Item.Properties()).setRegistryName(Reference.MOD_ID, "drivericon");


    public static final ItemLauncher LAUNCHER = new ItemLauncher("launcher", 1);
    public static final ItemLauncher LEFTLAUNCHER = new ItemLauncher("left_launcher", -1);
    public static final ItemDualLauncher DUALLAUNCHER = new ItemDualLauncher("dual_launcher");
    public static final ItemLauncherHandle LAUNCHERHANDLE = new ItemLauncherHandle("launcher_handle");
    public static final ItemBeyLogger BEYLOGGER = new ItemBeyLogger("beylogger");
    public static final ItemBeyLogger BEYLOGGERPLUS = new ItemBeyLogger("beylogger_plus");

    /**
     * Accessories
     */
	public static final AccessoryItem BARUTO_JACKET = new AccessoryItem( "baruto_jacket",
            EquipmentAccessoriesSlot.JACKET);
    public static final AccessoryItem BARUTO_PANTS = new AccessoryItem( "baruto_pants",
            EquipmentAccessoriesSlot.PANTS);
    public static final AccessoryItem BARUTO_BOOTS = new AccessoryItem( "baruto_boots",
            EquipmentAccessoriesSlot.SHOES);
    public static final AccessoryItem BARUTO_CHEST = new AccessoryItem( "baruto_chest",
            EquipmentAccessoriesSlot.TSHIRT);
    public static final AccessoryItem BARUTO_GLOVES = new AccessoryItem( "baruto_gloves",
            EquipmentAccessoriesSlot.GLOVES);

    public static final AccessoryItem BARUTO_JACKET_CHO_Z = new AccessoryItem( "baruto_jacket_cho_z",
            EquipmentAccessoriesSlot.JACKET);
    public static final AccessoryItem BARUTO_PANTS_CHO_Z = new AccessoryItem( "baruto_pants_cho_z",
            EquipmentAccessoriesSlot.PANTS);
    public static final AccessoryItem BARUTO_BOOTS_CHO_Z = new AccessoryItem( "baruto_boots_cho_z",
            EquipmentAccessoriesSlot.SHOES);
    public static final AccessoryItem BARUTO_CHEST_CHO_Z = new AccessoryItem( "baruto_chest_cho_z",
            EquipmentAccessoriesSlot.TSHIRT);
    public static final AccessoryItem BARUTO_GLOVES_CHO_Z = new AccessoryItem( "baruto_gloves_cho_z",
            EquipmentAccessoriesSlot.GLOVES);

    /* Blocks */
    public static final BeyCreatorBlock BEYCREATORBLOCK = new BeyCreatorBlock(Material.HEAVY_METAL, "beycreator");
    public static final ExpositoryBlock EXPOSITORY = new ExpositoryBlock(Material.HEAVY_METAL, "expository");
    public static final StadiumBlock STADIUM = new StadiumBlock(Material.METAL, "stadium");
    public static final RobotBlock ROBOT = new RobotBlock(Material.METAL, "robot");
    public static final BattleInformerBlock BATTLE_INFORMER = new BattleInformerBlock(Material.METAL,"battle_informer");

}
