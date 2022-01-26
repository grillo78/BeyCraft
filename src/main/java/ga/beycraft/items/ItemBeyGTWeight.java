package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ItemBeyGTWeight extends Item {

    private final float weight;
    private String displayName;

    public ItemBeyGTWeight(String name, String displayName, float weight) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTLAYERS).stacksTo(1));
        this.weight = weight;
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyCraftRegistry.ITEMSGTWEIGHT.add(this);
        this.displayName = displayName;
    }

    @Override
    public ITextComponent getName(ItemStack p_200295_1_) {
        return new StringTextComponent(displayName);
    }

    public float getWeight() {
        return weight;
    }
}
