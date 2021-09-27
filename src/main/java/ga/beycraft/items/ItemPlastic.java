package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyRegistry;
import ga.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemPlastic extends Item {
    public ItemPlastic(String name) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB).stacksTo(64));
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyRegistry.ITEMS.put(name,this);
    }
}
