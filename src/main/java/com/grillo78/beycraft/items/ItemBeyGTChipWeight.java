package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBeyGTChipWeight extends ItemBeyGTChip {

    private float weight;
    private float burst;

    public ItemBeyGTChipWeight(String name, float weight, float burst){
        super(name,weight,burst);
    }

    public float getWeight() {
        return weight;
    }

    public float getBurst() {
        return burst;
    }
}
