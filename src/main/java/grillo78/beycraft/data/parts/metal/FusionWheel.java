package grillo78.beycraft.data.parts.metal;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class FusionWheel extends Beypart {

    public FusionWheel(ResourceLocation id, CompoundTag compoundTag) {
        super(id, compoundTag);
    }

    public FusionWheel(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putString("type", "fusion_wheel");

        return compoundTag;
    }

    @Override
    public Item getPartItem() {
        return ModItems.METAL_FUSION_WHEEL.get();
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/metal/fusion_wheels/"+ getId().getPath() + ".obj");
    }
}
