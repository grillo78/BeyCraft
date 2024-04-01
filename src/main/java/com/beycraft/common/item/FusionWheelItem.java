package com.beycraft.common.item;

import com.beycraft.BeyTypes;
import com.beycraft.Beycraft;
import com.beycraft.client.item.GenericPartRenderer;
import com.beycraft.common.ability.AbilityType;
import com.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class FusionWheelItem extends BeyPartItem{
    public FusionWheelItem(String name, String displayName, float attack, float defense, float weight, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.LAYERS, new Properties().setISTER(()-> GenericPartRenderer::new));
    }

    public ResourceLocation getBookCategory() {
        return new ResourceLocation(Beycraft.MOD_ID, "layers");
    }

    @Override
    public boolean canBeCrafted(Item item) {
        return item == Items.IRON_INGOT;
    }
}
