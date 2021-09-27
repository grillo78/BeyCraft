package ga.beycraft.items;

import ga.beycraft.abilities.Ability;
import ga.beycraft.util.BeyTypes;

public class ItemBeyLayerDual extends ItemBeyLayer {

	public ItemBeyLayerDual(String name, float attack, float defense, float weight, float burst,
                            Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
		super(name, 1, attack, defense, weight, burst, primaryAbility, secundaryAbility, type);
	}

}
