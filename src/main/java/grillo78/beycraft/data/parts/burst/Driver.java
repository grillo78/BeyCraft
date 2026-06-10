package grillo78.beycraft.data.parts.burst;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Driver extends Beypart {
    private float friction = 1;
    private float height = 0;
    private float speed = 1;
    private float radiusReduction = 1;
    private float burstResistance = 1;

    public Driver(ResourceLocation id, CompoundTag compoundTag) {
        super(id, compoundTag);
        height = compoundTag.getFloat("height");
        friction = compoundTag.getFloat("friction");
        speed = compoundTag.getFloat("speed");
        radiusReduction = compoundTag.getFloat("radiusReduction");
        burstResistance = compoundTag.getFloat("burstResistance");
    }

    public Driver(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
        if (jsonObject.has("height"))
            height = jsonObject.get("height").getAsFloat();
        if (jsonObject.has("friction"))
            friction = jsonObject.get("friction").getAsFloat();
        if (jsonObject.has("speed"))
            speed = jsonObject.get("speed").getAsFloat();
        if (jsonObject.has("radiusReduction"))
            radiusReduction = jsonObject.get("radiusReduction").getAsFloat();
        if (jsonObject.has("burstResistance"))
            burstResistance = jsonObject.get("burstResistance").getAsFloat();
    }

    public float getRadiusReduction() {
        return radiusReduction;
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putFloat("height", height);
        compoundTag.putFloat("friction", friction);
        compoundTag.putFloat("speed", speed);
        compoundTag.putFloat("radiusReduction", radiusReduction);
        compoundTag.putFloat("burstResistance", burstResistance);
        compoundTag.putString("type", "driver");

        return compoundTag;
    }

    public float getSpeed() {
        return speed;
    }

    public float getBurstResistance() {
        return burstResistance;
    }

    public float getHeight() {
        return height;
    }

    public float getFriction() {
        return friction;
    }

    @Override
    public Item getPartItem() {
        return ModItems.BURST_DRIVER.get();
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/burst/drivers/" + getId().getPath() + ".obj");
    }
}
