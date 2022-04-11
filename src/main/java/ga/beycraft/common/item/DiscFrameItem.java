package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.ability.AbilityType;

public class DiscFrameItem extends DiscItem{

    private float frameRotation;

    public DiscFrameItem(String name, String displayName, float attack, float defense, float weight, float frameRotation, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, attack, defense, weight, primaryAbility, secondaryAbility, type);
        this.frameRotation = frameRotation;
    }

    public float getFrameRotation() {
        return frameRotation;
    }
}
