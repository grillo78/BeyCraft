package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.client.item.GenericPartRenderer;
import ga.beycraft.common.tab.BeycraftItemGroup;

public class FrameItem extends BeyPartItem{
    public FrameItem(String name, String displayName, float attack, float defense, BeyTypes type) {
        super(name, displayName, type, null, null, BeycraftItemGroup.DISCS, new Properties().setISTER(()-> GenericPartRenderer::new));
    }
}
