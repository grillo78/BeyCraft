package grillo78.beycraft.common.item;

import grillo78.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.item.Item;

public class HandleItem extends Item {
    public HandleItem(Properties properties) {
        super(properties.tab(BeycraftItemGroup.INSTANCE));
    }
}
