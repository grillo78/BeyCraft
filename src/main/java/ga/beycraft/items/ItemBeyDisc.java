package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.items.render.GenericPartItemStackRendererTileEntity;
import ga.beycraft.util.BeyTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBeyDisc extends ItemBeyPart{

	private float weight;
	private float attack;
	private float defense;

	public ItemBeyDisc(String name, String displayName, float attack, float defense, float weight, Ability primaryAbility, Ability secondaryAbility, BeyTypes type, Item.Properties properties) {
		super(name, displayName, type, primaryAbility, secondaryAbility, BeyCraft.BEYCRAFTDISKS,properties.setISTER(() -> GenericPartItemStackRendererTileEntity::new));
		this.weight = weight;
		this.attack = attack;
		this.defense = defense;
		BeyCraftRegistry.ITEMSDISCLIST.add(this);
	}

	public float getAttack(ItemStack stack) {
		return attack;
	}

	public float getDefense(ItemStack stack) {
		return defense;
	}

	public float getWeight() {
		return weight;
	}
}
