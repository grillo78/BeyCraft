package com.grillo78.BeyCraft;

import java.util.List;

import com.google.common.collect.Lists;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDriver;
import com.grillo78.BeyCraft.items.ItemBeyLayer;
import com.grillo78.BeyCraft.items.ItemBeyLogger;
import com.grillo78.BeyCraft.items.ItemBeyPackage;
import com.grillo78.BeyCraft.items.ItemLauncher;
import com.grillo78.BeyCraft.items.ItemLauncherHandle;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

@EventBusSubscriber(modid = Reference.MODID)
public class BeyRegistry {
	public static List<Item> ITEMS = Lists.newArrayList();
	public static List<Item> ITEMSLAYER = Lists.newArrayList();
	public static List<Item> ITEMSDISK = Lists.newArrayList();
	public static List<Item> ITEMSDRIVER = Lists.newArrayList();
	public static final EntityEntry BEYENTITY = new EntityEntry(EntityBey.class, "bey");
	public static final ItemBeyPackage BEYPACKAGE = new ItemBeyPackage("Package");
	public static final ItemBeyLayer ACHILLESA4 = new ItemBeyLayer("AchillesA4");
	public static final ItemBeyDisk ELEVENDISK = new ItemBeyDisk("11disk");
	public static final ItemBeyDriver XTENDDRIVER = new ItemBeyDriver("XtendDriver");
	public static final ItemBeyLayer VALTRYEKV4 = new ItemBeyLayer("ValtryekV4");
	public static final ItemBeyDisk TWELVEDISK = new ItemBeyDisk("12disk");
	public static final ItemBeyDriver VOLCANICDRIVER = new ItemBeyDriver("Volcanic_Driver");
	public static final ItemBeyLayer VALTRYEKV2 = new ItemBeyLayer("ValtryekV2");
	public static final ItemBeyDisk BOOSTDISK = new ItemBeyDisk("boostdisk");
	public static final ItemBeyDriver VARIABLEDRIVER = new ItemBeyDriver("Variable_Driver");
	public static final ItemBeyLayer VALTRYEKV5 = new ItemBeyLayer("ValtryekV5");
	public static final ItemBeyDisk ZENITHDISK = new ItemBeyDisk("zenithdisk");
	public static final ItemBeyDriver EVOLUTIONDRIVER = new ItemBeyDriver("evolution_driver");
	public static final ItemBeyLayer TURBOSPRYZEN = new ItemBeyLayer("TurboSpryzen");
	public static final ItemBeyDisk ZEROWDISK = new ItemBeyDisk("0wdisk");
	public static final ItemBeyDriver ZETASDRIVER = new ItemBeyDriver("zetas_driver");
	public static final ItemBeyLayer SALAMANDERS4 = new ItemBeyLayer("SalamanderS4");
	public static final ItemBeyLayer VALTRYEKV3 = new ItemBeyLayer("ValtryekV3");
	public static final ItemLauncher REDLAUNCHER = new ItemLauncher("Red_Launcher");
	public static final ItemLauncherHandle LAUNCHERHANDLE = new ItemLauncherHandle("LauncherHandle");
	public static final ItemBeyLogger BEYLOGGER = new ItemBeyLogger("Beylogger");
	public static final ItemBeyLogger BEYLOGGERPLUS = new ItemBeyLogger("Beylogger_Plus");
	static {
		BEYENTITY.setRegistryName(new ResourceLocation(Reference.MODID, "bey"));
		BEYENTITY.setEgg(new EntityEggInfo(new ResourceLocation(Reference.MODID, "bey") , 1, 2));
	}
	
	
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
		for(Item item : ITEMS) {
			if(item instanceof IHasModel) {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(),"inventory"));
			}
		}
		for(Item item : ITEMSLAYER) {
			if(item instanceof IHasModel) {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(),"inventory"));
			}
		}
		for(Item item : ITEMSDISK) {
			if(item instanceof IHasModel) {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(),"inventory"));
			}
		}
		for(Item item : ITEMSDRIVER) {
			if(item instanceof IHasModel) {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(),"inventory"));
			}
		}
		
//		for(Block block: BLOCKS) {
//			if(block instanceof IHasModel) {
//				((IHasModel)block).registerModels();
//			}
//		}
	}
	
}
