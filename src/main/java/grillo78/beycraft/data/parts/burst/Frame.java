package grillo78.beycraft.data.parts.burst;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Frame extends Beypart {
    public Frame(ResourceLocation id, CompoundTag compoundTag) {
        super(id, compoundTag);
    }

    public Frame(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
    }

    @Override
    public Item getPartItem() {
        return ModItems.BURST_FRAME.get();
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putString("type", "frame");

        return compoundTag;
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/burst/frames/"+ getId().getPath() + ".obj");
    }
}
