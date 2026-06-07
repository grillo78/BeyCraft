package grillo78.beycraft.data.parts;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.units.qual.C;

public abstract class Beypart {

    private final ResourceLocation id;
    private float weight = 1;

    public Beypart(ResourceLocation id, JsonObject jsonObject) {
        this.id = id;
        if(jsonObject.has("weight"))
            weight = jsonObject.get("weight").getAsFloat();
    }

    public Beypart(ResourceLocation id, CompoundTag compound) {
        this.id = id;
        this.weight = compound.getFloat("weight");
    }

    public float getWeight(ItemStack pieceStack) {
        return weight;
    }

    public ResourceLocation getId() {
        return id;
    }

    public abstract Item getPartItem();

    public abstract ResourceLocation getModel();

    public CompoundTag toCompound(){
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putFloat("weight", weight);
        compoundTag.putString("partId", id.toString());

        return compoundTag;
    }
}
