package com.beycraft.common.item;

import com.beycraft.BeyTypes;
import com.beycraft.Beycraft;
import com.beycraft.client.item.GenericPartRenderer;
import com.beycraft.common.ability.AbilityType;
import com.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.util.ResourceLocation;

public class DriverItem extends BeyPartItem{

    private float radiusReduction;
    private float friction;
    private float speed;

    public DriverItem(String name, String displayName, float friction, float radiusReduction, float speed, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.DRIVERS, new Properties().setISTER(()-> GenericPartRenderer::new));
        this.friction = friction;
        this.radiusReduction = radiusReduction;
        this.speed = speed;
    }

    public ResourceLocation getBookCategory() {
        return new ResourceLocation(Beycraft.MOD_ID, "drivers");
    }

    public float getRadiusReduction() {
        return radiusReduction;
    }

    public float getFriction() {
        return friction;
    }

    public double getSpeed() {
        return speed;
    }
}
