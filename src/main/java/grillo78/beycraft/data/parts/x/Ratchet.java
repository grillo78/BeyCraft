package grillo78.beycraft.data.parts.x;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Ratchet extends Beypart {

    public Ratchet(ResourceLocation id, CompoundTag compound) {
        super(id, compound);
    }

    public Ratchet(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putString("type", "ratchet");

        return compoundTag;
    }

    @Override
    public Item getPartItem() {
        return ModItems.X_RATCHET.get();
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/x/ratchets/" + getId().getPath() + ".obj");
    }
}
