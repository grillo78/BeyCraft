package grillo78.beycraft.data.parts.burst;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.items.ModItems;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CoreDisc extends Disc {

    public CoreDisc(ResourceLocation id, CompoundTag compoundTag) {
        super(id, compoundTag);
    }

    public CoreDisc(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
    }

    @Override
    public float getWeight(ItemStack pieceStack) {
        float frameWeight = 0;
        if(pieceStack.has(ModDataComponents.BURST_FRAME) && BeypartsReloadListener.BEYPARTS.get(pieceStack.get(ModDataComponents.BURST_FRAME)) != null)
            frameWeight = BeypartsReloadListener.BEYPARTS.get(pieceStack.get(ModDataComponents.BURST_FRAME)).getWeight(pieceStack.get(ModDataComponents.BURST_FRAME).getStack());
        return super.getWeight(pieceStack) + frameWeight;
    }

    @Override
    public CompoundTag toCompound() {
        CompoundTag compoundTag = super.toCompound();

        compoundTag.putString("type", "core_disc");

        return compoundTag;
    }
    @Override
    public Item getPartItem() {
        return ModItems.BURST_CORE_DISC.get();
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/burst/discs/"+ getId().getPath() + ".obj");
    }
}
