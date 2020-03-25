package com.grillo78.beycraft.items.armor;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBladerArmor extends ArmorItem {

	private String setName;

	public ItemBladerArmor(ArmorMaterial materialIn, String name, EquipmentSlotType equipmentSlotIn, String setName) {
		super(materialIn, equipmentSlotIn, new Item.Properties().group(BeyCraft.BEYCRAFTTAB));
		setRegistryName(new ResourceLocation(Reference.MODID,name));
		this.setName=setName;
		BeyRegistry.ITEMS.put(name,this);
	}

//	@SideOnly(Side.CLIENT)
//	@Override
//	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
//			ModelBiped _default) {
//		ModelBiped model;
//		switch (getUnlocalizedName()) {
//		case "Aiger_chestplate":
//		case "Aiger_leggings":
//		case "Aiger_boots":
//			model = new ModelAiger(1,armorSlot);
//			break;
//		case "valt_chestplate":
//		case "valt_leggings":
//		case "valt_boots":
//			model = new ModelAiger(1,armorSlot);
//			break;
//		case "turbo_valt_chestplate":
//		case "turbo_valt_leggings":
//		case "turbo_valt_boots":
//			model = new ModelAiger(1,armorSlot);
//			break;
//		default:
//			model = new ModelAiger(1,armorSlot);
//		}
//		return model;
//	}
//	
//	@Override
//	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
//		return Reference.MODID+":textures/entity/armor/"+setName+"_clothes.png";
//	}
}
