package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBeyGTChip extends Item {

    private float weight;
    private float burst;

    public ItemBeyGTChip(String name, float weight, float burst){
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTLAYERS).stacksTo(1));
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyCraftRegistry.ITEMSGTCHIP.add(this);
        this.burst = burst;
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public float getBurst() {
        return burst;
    }
}
