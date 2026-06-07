package grillo78.beycraft.data.parts.metal;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.items.ModItems;
import grillo78.beycraft.network.SyncBeyparts;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;

public class SpinTrack extends Beypart {

    private float height = 0;

    public SpinTrack(ResourceLocation id, CompoundTag compoundTag) {
        super(id, compoundTag);
        height = compoundTag.getFloat("height");
    }

    public SpinTrack(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
        if(jsonObject.has("height"))
            height = jsonObject.get("height").getAsFloat();
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putFloat("height", height);
        compoundTag.putString("type", "spin_track");

        return compoundTag;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public Item getPartItem() {
        return ModItems.METAL_SPIN_TRACK.get();
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/metal/spin_tracks/"+ getId().getPath() + ".obj");
    }
}
