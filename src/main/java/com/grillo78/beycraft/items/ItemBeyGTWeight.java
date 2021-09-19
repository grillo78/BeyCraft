package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBeyGTWeight extends Item {

    private final float weight;

    public ItemBeyGTWeight(String name, float weight) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTLAYERS).stacksTo(1));
        this.weight = weight;
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyRegistry.ITEMSGTWEIGHT.add(this);
    }

    public float getWeight() {
        return weight;
    }
}
