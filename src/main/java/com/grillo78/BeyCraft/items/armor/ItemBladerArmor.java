package com.grillo78.BeyCraft.items.armor;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.entity.ModelAiger;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBladerArmor extends ItemArmor implements IHasModel {

	private String setName;

	public ItemBladerArmor(ArmorMaterial materialIn, String name, EntityEquipmentSlot equipmentSlotIn, String setName) {
		super(materialIn, 0, equipmentSlotIn);
		this.setCreativeTab(BeyCraft.BEYCRAFTTAB);
		setRegistryName(new ResourceLocation(Reference.MODID,name));
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		this.setName=setName;
		BeyRegistry.ITEMS.add(this);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0,
				new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
			ModelBiped _default) {
		ModelBiped model;
		switch (getUnlocalizedName()) {
		case "Aiger_chestplate":
		case "Aiger_leggings":
		case "Aiger_boots":
			model = new ModelAiger(1,armorSlot);
			break;
		case "valt_chestplate":
		case "valt_leggings":
		case "valt_boots":
			model = new ModelAiger(1,armorSlot);
			break;
		case "turbo_valt_chestplate":
		case "turbo_valt_leggings":
		case "turbo_valt_boots":
			model = new ModelAiger(1,armorSlot);
			break;
		default:
			model = new ModelAiger(1,armorSlot);
		}
		return model;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return Reference.MODID+":textures/entity/armor/"+setName+"_clothes.png";
	}
}
