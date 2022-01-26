package ga.beycraft.items;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.util.BeyTypes;

public class ItemBeyLayerGTDual extends ItemBeyLayerGT {
    public ItemBeyLayerGTDual(String name, String displayName, float attack, float defense, float weight, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, displayName, 1, attack, defense, weight, primaryAbility, secundaryAbility, type);
        BeyCraftRegistry.ITEMSLAYERGT.add(this);
    }

}
