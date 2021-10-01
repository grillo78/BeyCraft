package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBeyGodChip extends Item {

    private float weight;

    public ItemBeyGodChip(String name, float weight){
        super(new Properties().tab(BeyCraft.BEYCRAFTLAYERS).stacksTo(1));
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyCraftRegistry.ITEMSGTCHIP.add(this);
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

}
