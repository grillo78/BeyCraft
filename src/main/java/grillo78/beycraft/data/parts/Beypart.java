package grillo78.beycraft.data.parts;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public abstract class Beypart {

    private final ResourceLocation id;

    public Beypart(ResourceLocation id, JsonObject jsonObject) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public abstract Item getPartItem();

    public abstract ResourceLocation getModel();
}
