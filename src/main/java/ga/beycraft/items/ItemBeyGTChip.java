package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ItemBeyGTChip extends Item {

    private float weight;
    private float burst;
    private ItemBeyLayer.Color resonanceColor;
    private ItemBeyLayer.Color secondResonanceColor;
    private ItemBeyLayer.Color thirdResonanceColor;
    private String displayName;

    public ItemBeyGTChip(String name, String displayName, float weight, float burst, ItemBeyLayer.Color resonanceColor, ItemBeyLayer.Color secondResonanceColor, ItemBeyLayer.Color thirdResonanceColor){
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTLAYERS).stacksTo(1));
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyCraftRegistry.ITEMSGTCHIP.add(this);
        this.burst = burst;
        this.weight = weight;
        this.resonanceColor = resonanceColor;
        this.secondResonanceColor = secondResonanceColor;
        this.thirdResonanceColor = thirdResonanceColor;
        this.displayName = displayName;
    }

    @Override
    public ITextComponent getName(ItemStack p_200295_1_) {
        return new StringTextComponent(displayName);
    }

    public ItemBeyLayer.Color getResonanceColor() {
        return resonanceColor;
    }

    public float getWeight() {
        return weight;
    }

    public float getBurst() {
        return burst;
    }

    public ItemBeyLayer.Color getSecondResonanceColor() {
        return secondResonanceColor;
    }

    public ItemBeyLayer.Color getThirdResonanceColor() {
        return thirdResonanceColor;
    }
}
