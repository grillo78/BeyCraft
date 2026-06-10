package grillo78.beycraft.data.parts.burst;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Disc extends Beypart {

    private float attack = 0;
    private float defense = 0;

    public Disc(ResourceLocation id, CompoundTag compound) {
        super(id, compound);
        attack = compound.getFloat("attack");
        defense = compound.getFloat("defense");
    }

    public Disc(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
        if (jsonObject.has("attack"))
            attack = jsonObject.get("attack").getAsFloat();
        if (jsonObject.has("defense"))
            defense = jsonObject.get("defense").getAsFloat();
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putString("type", "disc");
        compoundTag.putString("attack", "attack");
        compoundTag.putString("defense", "defense");

        return compoundTag;
    }

    public float getAttack() {
        return attack;
    }

    public float getDefense() {
        return defense;
    }

    @Override
    public Item getPartItem() {
        return ModItems.BURST_DISC.get();
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/burst/discs/" + getId().getPath() + ".obj");
    }
}
