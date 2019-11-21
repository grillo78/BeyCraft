package com.grillo78.BeyCraft;

import java.util.List;

import com.google.common.collect.Lists;
import com.grillo78.BeyCraft.entity.AigerModel;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDriver;
import com.grillo78.BeyCraft.items.ItemBeyLayer;
import com.grillo78.BeyCraft.items.ItemBeyLogger;
import com.grillo78.BeyCraft.items.ItemBeyPackage;
import com.grillo78.BeyCraft.items.ItemLauncher;
import com.grillo78.BeyCraft.items.ItemLauncherHandle;
import com.grillo78.BeyCraft.items.armor.ItemBladerArmor;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@EventBusSubscriber(modid = Reference.MODID)
public class BeyRegistry {
	public static List<Block> BLOCKS = Lists.newArrayList();
	public static List<Item> ITEMS = Lists.newArrayList();
	public static List<Item> ITEMSLAYER = Lists.newArrayList();
	public static List<Item> ITEMSDISK = Lists.newArrayList();
	public static List<Item> ITEMSDRIVER = Lists.newArrayList();

	/* Entity */
	public static final EntityEntry BEYENTITY = EntityEntryBuilder.create().entity(EntityBey.class)
			.id(new ResourceLocation(Reference.MODID, "bey"), 33).name("bey").tracker(160, 2, false).build();

	/* ArmorMaterials */
	public static final ArmorMaterial AIGER_MATERIAL = EnumHelper.addArmorMaterial("Aiger_model",
			Reference.MODID + ":Aiger_clothes", 1000, new int[] { 10, 10, 10, 10 }, 0,
			SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0F);

	/* Items */
	public static final ItemBeyPackage BEYPACKAGE = new ItemBeyPackage("Package");
	public static final ItemBeyLayer ACHILLESA4 = new ItemBeyLayer("AchillesA4",-0.08F, 1);
	public static final ItemBeyDisk ELEVENDISK = new ItemBeyDisk("11disk",-0.15F);
	public static final ItemBeyDriver XTENDDRIVER = new ItemBeyDriver("XtendDriver", 0.15F);
	public static final ItemBeyLayer VALTRYEKV4 = new ItemBeyLayer("ValtryekV4",-0.08F, 1);
	public static final ItemBeyDisk TWELVEDISK = new ItemBeyDisk("12disk",-0.15F);
	public static final ItemBeyDriver VOLCANICDRIVER = new ItemBeyDriver("Volcanic_Driver", 0.15F);
	public static final ItemBeyLayer VALTRYEKV2 = new ItemBeyLayer("ValtryekV2",-0.08F, 1);
	public static final ItemBeyDisk BOOSTDISK = new ItemBeyDisk("boostdisk",-0.15F);
	public static final ItemBeyDriver VARIABLEDRIVER = new ItemBeyDriver("Variable_Driver", 0.15F);
	public static final ItemBeyLayer VALTRYEKV5 = new ItemBeyLayer("ValtryekV5",-0.15F, 1);
	public static final ItemBeyDisk ZENITHDISK = new ItemBeyDisk("zenithdisk",-0.15F);
	public static final ItemBeyDriver EVOLUTIONDRIVER = new ItemBeyDriver("evolution_driver", 0.15F);
	public static final ItemBeyLayer TURBOSPRYZEN = new ItemBeyLayer("TurboSpryzen",-0.08F, 1);
	public static final ItemBeyDisk ZEROWDISK = new ItemBeyDisk("0wdisk",-0.1F);
	public static final ItemBeyDriver ZETASDRIVER = new ItemBeyDriver("zetas_driver", 0.15F);
	public static final ItemBeyLayer SALAMANDERS4 = new ItemBeyLayer("SalamanderS4",-0.08F, -1);
	public static final ItemBeyLayer VALTRYEKV3 = new ItemBeyLayer("ValtryekV3",-0.08F, 1);
	public static final ItemLauncher REDLAUNCHER = new ItemLauncher("Red_Launcher");
	public static final ItemLauncherHandle LAUNCHERHANDLE = new ItemLauncherHandle("LauncherHandle");
	public static final ItemBeyLogger BEYLOGGER = new ItemBeyLogger("Beylogger");
	public static final ItemBeyLogger BEYLOGGERPLUS = new ItemBeyLogger("Beylogger_Plus");
	public static final ItemBladerArmor AIGER_CHESTPLATE = new ItemBladerArmor(AIGER_MATERIAL, "Aiger_cheastplate",
			EntityEquipmentSlot.CHEST, new AigerModel());
	public static final ItemBladerArmor AIGER_LEGGINS = new ItemBladerArmor(AIGER_MATERIAL, "Aiger_leggins",
			EntityEquipmentSlot.LEGS, new AigerModel());
	public static final ItemBladerArmor AIGER_BOOTS = new ItemBladerArmor(AIGER_MATERIAL, "Aiger_boots",
			EntityEquipmentSlot.FEET, new AigerModel());

	@SubscribeEvent
	public static void registerBey(RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().register(BEYENTITY);
	}

	@SubscribeEvent
	public static void registerItem(RegistryEvent.Register<Item> event) {
		for (Item item : ITEMS) {
			event.getRegistry().register(item);
		}
		for (Item item : ITEMSLAYER) {
			event.getRegistry().register(item);
		}
		for (Item item : ITEMSDISK) {
			event.getRegistry().register(item);
		}
		for (Item item : ITEMSDRIVER) {
			event.getRegistry().register(item);
		}
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : ITEMS) {
			if (item instanceof IHasModel) {
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}
		for (Item item : ITEMSLAYER) {
			if (item instanceof IHasModel) {
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}
		for (Item item : ITEMSDISK) {
			if (item instanceof IHasModel) {
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}
		for (Item item : ITEMSDRIVER) {
			if (item instanceof IHasModel) {
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}
	}

}
