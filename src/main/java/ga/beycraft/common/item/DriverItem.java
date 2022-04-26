package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.ability.AbilityType;
import ga.beycraft.client.item.GenericPartRenderer;
import ga.beycraft.common.tab.BeycraftItemGroup;

public class DriverItem extends BeyPartItem{

    private float radiusReduction;
    private float friction;

    public DriverItem(String name, String displayName, float friction, float radiusReduction, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.DRIVERS, new Properties().setISTER(()-> GenericPartRenderer::new));
        this.friction = friction;
        this.radiusReduction = radiusReduction;
    }

    public float getRadiusReduction() {
        return radiusReduction;
    }

    public float getFriction() {
        return friction;
    }
}
