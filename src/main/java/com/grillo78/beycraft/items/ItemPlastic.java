package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemPlastic extends Item {
    public ItemPlastic(String name) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB).stacksTo(64));
        this.setRegistryName(new ResourceLocation(Reference.MODID, name));
        BeyRegistry.ITEMS.put(name,this);
    }
}
