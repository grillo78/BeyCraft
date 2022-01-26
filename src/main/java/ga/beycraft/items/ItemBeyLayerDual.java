package ga.beycraft.items;

import ga.beycraft.abilities.Ability;
import ga.beycraft.util.BeyTypes;

public class ItemBeyLayerDual extends ItemBeyLayer {

	public ItemBeyLayerDual(String name, String displayName, float attack, float defense, float weight, float burst,
                            Ability primaryAbility, Ability secundaryAbility, BeyTypes type, Color color, Color color2, Color color3) {
		super(name, displayName, 1, attack, defense, weight, burst, primaryAbility, secundaryAbility, type, color, color2, color3);
	}

}
