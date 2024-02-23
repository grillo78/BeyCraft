package com.beycraft.common.item;

import com.beycraft.BeyTypes;
import com.beycraft.client.item.GenericPartRenderer;
import com.beycraft.common.tab.BeycraftItemGroup;

public class GTWeightItem extends BeyPartItem {

    private float weight;
    public GTWeightItem(String name, String displayName, Float weight, BeyTypes type) {
        super(name, displayName, type, null, null, BeycraftItemGroup.LAYERS, new Properties().setISTER(()-> GenericPartRenderer::new));
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }
}
