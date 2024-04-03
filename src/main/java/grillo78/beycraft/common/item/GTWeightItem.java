package grillo78.beycraft.common.item;

import grillo78.beycraft.BeyTypes;
import grillo78.beycraft.client.item.GenericPartRenderer;
import grillo78.beycraft.common.tab.BeycraftItemGroup;

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
