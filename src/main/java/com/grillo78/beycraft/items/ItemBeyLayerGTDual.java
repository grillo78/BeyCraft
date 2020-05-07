package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.util.BeyTypes;

public class ItemBeyLayerGTDual extends ItemBeyLayerGT {
    public ItemBeyLayerGTDual(String name, float attack, float defense, float weight, float burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, 1, attack, defense, weight, burst, primaryAbility, secundaryAbility, type);
        BeyRegistry.ITEMSLAYERGT.add(this);
    }
}
