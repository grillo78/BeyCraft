package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBeyGTChip extends Item {

    public ItemBeyGTChip(String name){
        super(new Item.Properties().group(BeyCraft.BEYCRAFTLAYERS).maxStackSize(1));
        setRegistryName(new ResourceLocation(Reference.MODID, name));
        BeyRegistry.ITEMSGTCHIP.add(this);
    }
}
