package grillo78.beycraft.data.parts.metal;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EnergyRing extends Beypart {

    public EnergyRing(ResourceLocation id, CompoundTag compoundTag) {
        super(id, compoundTag);
    }

    public EnergyRing(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
    }

    @Override
    public Item getPartItem() {
        return ModItems.METAL_ENERGY_RING.get();
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putString("type", "energy_ring");

        return compoundTag;
    }
    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/metal/energy_rings/" + getId().getPath().replace("_energy_ring", "") + ".obj");
    }
}
