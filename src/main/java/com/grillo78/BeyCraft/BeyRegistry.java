package com.grillo78.BeyCraft;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.grillo78.BeyCraft.blocks.ExpositoryBlock;
import com.grillo78.BeyCraft.blocks.StadiumBlock;
import com.grillo78.BeyCraft.capabilities.Provider;
import com.grillo78.BeyCraft.entity.AigerModel;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDriver;
import com.grillo78.BeyCraft.items.ItemBeyLayer;
import com.grillo78.BeyCraft.items.ItemBeyLayerDual;
import com.grillo78.BeyCraft.items.ItemBeyLogger;
import com.grillo78.BeyCraft.items.ItemBeyPackage;
import com.grillo78.BeyCraft.items.ItemLauncher;
import com.grillo78.BeyCraft.items.ItemLauncherHandle;
import com.grillo78.BeyCraft.items.armor.ItemBladerArmor;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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
	public static final ArmorMaterial BLADER_MATERIAL = EnumHelper.addArmorMaterial("blader_model",
			Reference.MODID + ":blader_clothes", 1000, new int[] { 10, 10, 10, 10 }, 0,
			SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0F);

	/* Items */
	public static final ItemBeyPackage BEYPACKAGE = new ItemBeyPackage("Package");
	public static final ItemBeyLayer ACHILLESA4 = new ItemBeyLayer("AchillesA4", -0.08F, 1, false);
	public static final ItemBeyDisk ELEVENDISK = new ItemBeyDisk("11disk", -0.15F);
	public static final ItemBeyDriver XTENDDRIVER = new ItemBeyDriver("XtendDriver", 0.15F, 1, 1);
	public static final ItemBeyLayer FAFNIRF4 = new ItemBeyLayer("wizard_fafnir", -0.08F, -1, true);
	public static final ItemBeyDisk RATCHETDISK = new ItemBeyDisk("ratchet", -0.15F);
	public static final ItemBeyDriver RISEDRIVER = new ItemBeyDriver("rise_driver", 0.15F, 1, 1);
	public static final ItemBeyLayer VALTRYEKV4 = new ItemBeyLayer("ValtryekV4", -0.13F, 1, false);
	public static final ItemBeyDisk TWELVEDISK = new ItemBeyDisk("12disk", -0.15F);
	public static final ItemBeyDriver VOLCANICDRIVER = new ItemBeyDriver("Volcanic_Driver", 0.15F, 1, 1);
	public static final ItemBeyLayer VALTRYEKV2 = new ItemBeyLayer("ValtryekV2", -0.08F, 1, false);
	public static final ItemBeyDisk BOOSTDISK = new ItemBeyDisk("boostdisk", -0.15F);
	public static final ItemBeyDriver VARIABLEDRIVER = new ItemBeyDriver("Variable_Driver", 0.15F, 1, 1);
	public static final ItemBeyLayer VALTRYEKV5 = new ItemBeyLayer("ValtryekV5", -0.15F, 1, false);
	public static final ItemBeyDisk ZENITHDISK = new ItemBeyDisk("zenithdisk", -0.15F);
	public static final ItemBeyDriver EVOLUTIONDRIVER = new ItemBeyDriver("evolution_driver", 0.15F, 1, 1);
	public static final ItemBeyLayer REQUIEMSPRYZEN = new ItemBeyLayerDual("requiem_spryzen", -0.23F, true);
	public static final ItemBeyDisk ZERODISK = new ItemBeyDisk("0disk", -0.15F);
	public static final ItemBeyLayer TURBOSPRYZEN = new ItemBeyLayerDual("TurboSpryzen", -0.12F, false);
	public static final ItemBeyDisk ZEROWDISK = new ItemBeyDisk("0wdisk", -0.15F);
	public static final ItemBeyDriver ZETASDRIVER = new ItemBeyDriver("zetas_driver", 0.15F, 1, 1);
	public static final ItemBeyLayer SALAMANDERS4 = new ItemBeyLayer("SalamanderS4", -0.08F, -1, false);
	public static final ItemBeyDriver OPERATEDRIVER = new ItemBeyDriver("operate_driver", 0.15F, 1, 1);
	public static final ItemBeyLayer VALTRYEKV3 = new ItemBeyLayer("ValtryekV3", -0.08F, 1, false);
	public static final ItemLauncher REDLAUNCHER = new ItemLauncher("Red_Launcher", 1);
	public static final ItemLauncher LEFTLAUNCHER = new ItemLauncher("Left_Launcher", -1);
	public static final ItemLauncherHandle LAUNCHERHANDLE = new ItemLauncherHandle("LauncherHandle");
	public static final ItemBeyLogger BEYLOGGER = new ItemBeyLogger("Beylogger");
	public static final ItemBeyLogger BEYLOGGERPLUS = new ItemBeyLogger("Beylogger_Plus");
	public static final ItemBladerArmor AIGER_CHESTPLATE = new ItemBladerArmor(BLADER_MATERIAL, "Aiger_chestplate",
			EntityEquipmentSlot.CHEST, new AigerModel());
	public static final ItemBladerArmor AIGER_LEGGINGS = new ItemBladerArmor(BLADER_MATERIAL, "Aiger_leggings",
			EntityEquipmentSlot.LEGS, new AigerModel());
	public static final ItemBladerArmor AIGER_BOOTS = new ItemBladerArmor(BLADER_MATERIAL, "Aiger_boots",
			EntityEquipmentSlot.FEET, new AigerModel());
	public static final ItemBladerArmor VALT_CHESTPLATE = new ItemBladerArmor(BLADER_MATERIAL, "valt_chestplate",
			EntityEquipmentSlot.CHEST, new AigerModel());
	public static final ItemBladerArmor VALT_LEGGINGS = new ItemBladerArmor(BLADER_MATERIAL, "valt_leggings",
			EntityEquipmentSlot.LEGS, new AigerModel());
	public static final ItemBladerArmor VALT_BOOTS = new ItemBladerArmor(BLADER_MATERIAL, "valt_boots",
			EntityEquipmentSlot.FEET, new AigerModel());
	public static final ItemBladerArmor TURBO_VALT_CHESTPLATE = new ItemBladerArmor(BLADER_MATERIAL,
			"turbo_valt_chestplate", EntityEquipmentSlot.CHEST, new AigerModel());
	public static final ItemBladerArmor TURBO_VALT_LEGGINGS = new ItemBladerArmor(BLADER_MATERIAL,
			"turbo_valt_leggings", EntityEquipmentSlot.LEGS, new AigerModel());
	public static final ItemBladerArmor TURBO_VALT_BOOTS = new ItemBladerArmor(BLADER_MATERIAL, "turbo_valt_boots",
			EntityEquipmentSlot.FEET, new AigerModel());

	/* Blocks */
	public static final ExpositoryBlock EXPOSITORY = new ExpositoryBlock(Material.ANVIL, "Expository");
	public static final StadiumBlock STADIUM = new StadiumBlock(Material.IRON, "Stadium");

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
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		for (Block block : BLOCKS) {
			event.getRegistry().register(block);
		}
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : ITEMS) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		}
		for (Item item : ITEMSLAYER) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		}
		for (Item item : ITEMSDISK) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		}
		for (Item item : ITEMSDRIVER) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		}
		for (Block block : BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModels();
			}
		}
	}

	@SubscribeEvent
	public static void playerCapabilitiesInjection(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(new ResourceLocation(Reference.MODID, "BladerLevel"), new Provider());
		}
	}

	@SubscribeEvent
	public static void editHud(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL) {
			Minecraft.getMinecraft().renderEngine
					.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/bladerlevel.png"));
			drawTexturedModalRect(0, 0, 0, 0, 140, 140);
			Minecraft.getMinecraft().fontRenderer.drawString("Blader level:"
					+ Minecraft.getMinecraft().player.getCapability(Provider.BLADERLEVEL_CAP, null).getBladerLevel(), 3,
					10, 16777215);
		}
	}

	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos((double) (x + 0), (double) (y + height), 0.0D)
				.tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
		;
		buffer.pos((double) (x + width), (double) (y + height), 0.0D)
				.tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1))
				.endVertex();
		buffer.pos((double) (x + width), (double) (y + 0), 0.0D)
				.tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		buffer.pos((double) (x + 0), (double) (y + 0), 0.0D)
				.tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		tessellator.draw();
	}

}
