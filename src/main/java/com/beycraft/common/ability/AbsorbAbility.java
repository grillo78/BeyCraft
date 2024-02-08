package com.beycraft.common.ability;

import com.beycraft.common.entity.BeybladeEntity;
import net.minecraft.util.DamageSource;

public class AbsorbAbility extends Ability {

    @Override
    public void collideOtherBey(BeybladeEntity current, BeybladeEntity other) {
        if (current.getEnergy() < 10 && current.getRandom().nextInt(100)<10)
            other.hurt(DamageSource.mobAttack(current), other.getHealth());
    }
}
