package grillo78.beycraft.common.ability;

import grillo78.beycraft.common.entity.BeybladeEntity;
import net.minecraft.util.DamageSource;

public class AbsorbAbility extends Ability {

    @Override
    public void collideOtherBey(BeybladeEntity current, BeybladeEntity other) {
        if (current.getEnergy() < 10 && current.getRandom().nextInt(100)<10)
            other.hurt(DamageSource.mobAttack(current), other.getHealth());
    }
}
