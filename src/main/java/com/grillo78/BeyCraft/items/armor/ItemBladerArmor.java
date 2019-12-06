package com.grillo78.BeyCraft.items.armor;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.entity.AigerModel;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBladerArmor extends ItemArmor implements IHasModel {

	public ItemBladerArmor(ArmorMaterial materialIn, String name, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, 0, equipmentSlotIn);
		this.setCreativeTab(BeyCraft.BEYCRAFTTAB);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
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
			model = new AigerModel();
			break;
		case "valt_chestplate":
		case "valt_leggings":
		case "valt_boots":
			model = new AigerModel();
			break;
		case "turbo_valt_chestplate":
		case "turbo_valt_leggings":
		case "turbo_valt_boots":
			model = new AigerModel();
			break;
		default:
			model = new AigerModel();
		}
		if (itemStack != ItemStack.EMPTY) {
			if (itemStack.getItem() instanceof ItemArmor) {
				model.bipedRightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
				model.isChild = _default.isChild;
				model.isRiding = _default.isRiding;
				model.isSneak = _default.isSneak;
				return model;
			}
		}
		return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
	}
}
