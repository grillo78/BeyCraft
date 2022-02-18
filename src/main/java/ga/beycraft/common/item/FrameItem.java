package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.common.tab.BeycraftCreativeModeTab;

public class FrameItem extends BeyPartItem{
    public FrameItem(String name, String displayName, float attack, float defense, BeyTypes type) {
        super(name, displayName, type, null, null, BeycraftCreativeModeTab.DISCS, new Properties());
    }
}
