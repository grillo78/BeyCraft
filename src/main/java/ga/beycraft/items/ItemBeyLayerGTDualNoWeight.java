package ga.beycraft.items;

import ga.beycraft.BeyRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.util.BeyTypes;

public class ItemBeyLayerGTDualNoWeight extends ItemBeyLayerGTNoWeight {
    public ItemBeyLayerGTDualNoWeight(String name, float attack, float defense, float weight, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, 1, attack, defense, weight, primaryAbility, secundaryAbility, type);
        BeyRegistry.ITEMSLAYERGT.add(this);
    }
}
