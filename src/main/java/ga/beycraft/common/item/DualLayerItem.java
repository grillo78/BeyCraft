package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.ability.AbilityType;

public class DualLayerItem extends LayerItem{
    public DualLayerItem(String name, String displayName, float attack, float defense, float weight, float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, 1, attack, defense, weight, burst, primaryAbility, secondaryAbility, type, resonanceColor, secondResonanceColor, thirdResonanceColor);
    }
}
