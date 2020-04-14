package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.util.BeyTypes;

public class ItemBeyLayerGT extends ItemBeyLayer {
    public ItemBeyLayerGT(String name, float rotationDirection, float attack, float defense, float weight, float burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, rotationDirection, attack, defense, weight, burst, primaryAbility, secundaryAbility, type);
        BeyRegistry.ITEMSLAYERGT.add(this);
    }
}
