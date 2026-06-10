package grillo78.beycraft.data.parts;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Launcher {

    private final ResourceLocation id;
    private final ResourceLocation texture;
    private final ResourceLocation model;
    private final grillo78.beycraft.items.Launcher.Generations generation;

    public Launcher(ResourceLocation id, JsonObject jsonObject) {
        this.id = id;
        generation = grillo78.beycraft.items.Launcher.Generations.valueOf(jsonObject.get("generation").getAsString());
        texture = jsonObject.has("texture") ? ResourceLocation.parse(jsonObject.get("texture").getAsString()) : null;
        model = jsonObject.has("model") ? ResourceLocation.parse(jsonObject.get("model").getAsString()) : ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "launchers/" + getId().getPath() + ".obj");
    }

    public Launcher(ResourceLocation id, CompoundTag compound) {
        this.id = id;
        generation = grillo78.beycraft.items.Launcher.Generations.valueOf(compound.getString("generation"));
        texture = compound.contains("texture") ? ResourceLocation.parse(compound.getString("texture")) : null;
        model = ResourceLocation.parse(compound.getString("model"));
    }

    public ResourceLocation getId() {
        return id;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public Item getPartItem() {
        return null;
    }

    public grillo78.beycraft.items.Launcher.Generations getGeneration() {
        return generation;
    }

    public ResourceLocation getModel() {
        return model;
    }

    public CompoundTag toCompound() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putString("launcherId", id.toString());
        compoundTag.putString("generation", generation.getSerializedName());
        if (texture != null)
            compoundTag.putString("texture", texture.toString());
        compoundTag.putString("model", model.toString());

        return compoundTag;
    }
}
