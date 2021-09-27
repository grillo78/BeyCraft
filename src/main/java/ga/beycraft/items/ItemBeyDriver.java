package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.abilities.MultiMode;
import ga.beycraft.abilities.MultiType;
import ga.beycraft.items.render.GenericPartItemStackRendererTileEntity;
import ga.beycraft.util.BeyTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBeyDriver extends ItemBeyPart {

	private float friction;
	private float radiusReduction;

	public ItemBeyDriver(String name, float friction, float radiusReduction, Ability primaryAbility,
			Ability secundaryAbility, BeyTypes type) {
		super(name, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTDRIVERS, new Item.Properties().setISTER(() -> GenericPartItemStackRendererTileEntity::new));
		this.radiusReduction = radiusReduction;
		this.friction = friction;
		BeyRegistry.ITEMSDRIVER.add(this);
	}

	public float getRadiusReduction(ItemStack stack) {
		if (PRIMARYABILITY instanceof MultiType) {
			if (stack.hasTag() && stack.getTag().contains("Type")) {
				return ((MultiType) PRIMARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type"))
						.getValues()[1];
			}
			return ((MultiType) PRIMARYABILITY).getTypes().get(0).getValues()[1];
		}
		if (SECUNDARYABILITY instanceof MultiType) {
			if (stack.hasTag() && stack.getTag().contains("Type")) {
				return ((MultiType) SECUNDARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type"))
						.getValues()[1];
			}
			return ((MultiType) SECUNDARYABILITY).getTypes().get(0).getValues()[1];
		}
		if (PRIMARYABILITY instanceof MultiMode) {
			if (stack.hasTag() && stack.getTag().contains("Mode")) {
				return ((MultiMode) PRIMARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
						.getValues()[1];
			}
			return ((MultiMode) PRIMARYABILITY).getModes().get(0).getValues()[1];
		}
		if (SECUNDARYABILITY instanceof MultiMode) {
			if (stack.hasTag() && stack.getTag().contains("Mode")) {
				return ((MultiMode) SECUNDARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
						.getValues()[1];
			}
			return ((MultiMode) SECUNDARYABILITY).getModes().get(0).getValues()[1];
		}
		return radiusReduction;
	}

	public float getFriction(ItemStack stack) {
		if (PRIMARYABILITY instanceof MultiType) {
			if (stack.hasTag() && stack.getTag().contains("Type")) {
				return ((MultiType) PRIMARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type"))
						.getValues()[0];
			}
			return ((MultiType) PRIMARYABILITY).getTypes().get(0).getValues()[0];
		}
		if (SECUNDARYABILITY instanceof MultiType) {
			if (stack.hasTag() && stack.getTag().contains("Type")) {
				return ((MultiType) SECUNDARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type"))
						.getValues()[0];
			}
			return ((MultiType) SECUNDARYABILITY).getTypes().get(0).getValues()[0];
		}
		if (PRIMARYABILITY instanceof MultiMode) {
			if (stack.hasTag() && stack.getTag().contains("Mode")) {
				return ((MultiMode) PRIMARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
						.getValues()[1];
			}
			return ((MultiMode) PRIMARYABILITY).getModes().get(0).getValues()[0];
		}
		if (SECUNDARYABILITY instanceof MultiMode) {
			if (stack.hasTag() && stack.getTag().contains("Mode")) {
				return ((MultiMode) SECUNDARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
						.getValues()[1];
			}
			return ((MultiMode) SECUNDARYABILITY).getModes().get(0).getValues()[0];
		}
		return friction;
	}
}
