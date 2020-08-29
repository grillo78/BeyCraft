package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBeyGTChip extends Item {

    private float weight;
    private float burst;

    public ItemBeyGTChip(String name, float weight, float burst){
        super(new Item.Properties().group(BeyCraft.BEYCRAFTLAYERS).maxStackSize(1));
        setRegistryName(new ResourceLocation(Reference.MODID, name));
        BeyRegistry.ITEMSGTCHIP.add(this);
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
