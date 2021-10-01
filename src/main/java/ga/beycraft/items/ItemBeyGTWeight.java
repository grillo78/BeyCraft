package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBeyGTWeight extends Item {

    private final float weight;

    public ItemBeyGTWeight(String name, float weight) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTLAYERS).stacksTo(1));
        this.weight = weight;
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyCraftRegistry.ITEMSGTWEIGHT.add(this);
    }

    public float getWeight() {
        return weight;
    }
}
