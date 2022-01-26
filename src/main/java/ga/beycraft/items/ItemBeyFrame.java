package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.items.render.GenericPartItemStackRendererTileEntity;
import ga.beycraft.util.BeyTypes;
import net.minecraft.item.Item;

public class ItemBeyFrame extends ItemBeyPart{

	private float attack;
	private float defense;

	public ItemBeyFrame(String name, String displayName, float attack, float defense, BeyTypes type) {
		super(name, displayName, type, null, null, BeyCraft.BEYCRAFTDISKS,new Item.Properties().setISTER(()-> GenericPartItemStackRendererTileEntity::new));
		BeyCraftRegistry.ITEMSFRAME.add(this);
	}

	public float getAttack() {
		return attack;
	}

	public float getDefense() {
		return defense;
	}
}
