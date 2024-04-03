package grillo78.beycraft.common.ability;

import grillo78.beycraft.common.entity.BeybladeEntity;
import net.minecraft.item.ItemStack;

public abstract class Ability {
    public void tick(BeybladeEntity bey){}

    public void collideOtherBey(BeybladeEntity current, BeybladeEntity other){}

    public void itemUse(ItemStack stack){}
}
