package grillo78.beycraft.data.parts.burst;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.data.parts.metal.EnergyRing;
import grillo78.beycraft.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Layer extends Beypart {

    private float attack = 0;
    private float defense = 0;
    private float burstResistance = 0;

    public Layer(ResourceLocation id, CompoundTag compound) {
        super(id, compound);
        attack = compound.getFloat("attack");
        defense = compound.getFloat("defense");
        burstResistance = compound.getFloat("burstResistance");
    }

    public Layer(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
        if (jsonObject.has("attack"))
            attack = jsonObject.get("attack").getAsFloat();
        if (jsonObject.has("defense"))
            defense = jsonObject.get("defense").getAsFloat();
        if (jsonObject.has("burstResistance"))
            burstResistance = jsonObject.get("burstResistance").getAsFloat();
    }

    public float getBurstResistance() {
        return burstResistance;
    }

    public float getAttack() {
        return attack;
    }

    public float getDefense() {
        return defense;
    }

    @Override

    public Item getPartItem() {
        return ModItems.BURST_LAYER.get();
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putString("type", "layer");
        compoundTag.putFloat("attack", attack);
        compoundTag.putFloat("burstResistance", burstResistance);

        return compoundTag;
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/burst/layers/" + getId().getPath() + ".obj");
    }
}
