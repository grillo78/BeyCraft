package com.beycraft.common.ability;

import com.beycraft.common.entity.BeybladeEntity;
import net.minecraft.item.ItemStack;

public abstract class Ability {
    public void tick(BeybladeEntity bey){}

    public void collideOtherBey(BeybladeEntity current, BeybladeEntity other){}

    public void itemUse(ItemStack stack){}
}
