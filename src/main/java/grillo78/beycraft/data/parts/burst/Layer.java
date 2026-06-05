package grillo78.beycraft.data.parts.burst;

import com.google.gson.JsonObject;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.items.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Layer extends Beypart {

    public Layer(ResourceLocation id, JsonObject jsonObject) {
        super(id, jsonObject);
    }

    @Override
    public Item getPartItem() {
        return ModItems.BURST_LAYER.get();
    }

    @Override
    public ResourceLocation getModel() {
        return ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "beyparts/burst/layers/"+ getId().getPath() + ".obj");
    }
}
