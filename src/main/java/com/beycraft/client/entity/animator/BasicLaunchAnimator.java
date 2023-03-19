package com.beycraft.client.entity.animator;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.capability.Parkourability;
import net.minecraft.entity.player.PlayerEntity;

public class BasicLaunchAnimator extends Animator {

    private static float MAX_TICKS = 70;

    @Override
    public boolean shouldRemoved(PlayerEntity playerEntity, Parkourability parkourability) {
        return getTick() >= MAX_TICKS;
    }

    @Override
    public boolean animatePre(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
        return false;
    }

    @Override
    public void animatePost(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
        super.animatePost(player, parkourability, transformer);
        if(getTick()<10) {
            transformer.rotateRightArm((float) -(Math.PI / 2) * (getTick() / 10F), 0, 0).rotateLeftArm((float) -(Math.PI / 2) * (getTick() / 10F), (float) (Math.PI / 4 * (getTick() / 10F)), 0);
        }
        else if(getTick()<60)
            transformer.rotateRightArm((float) -Math.PI / 2, 0, 0).rotateLeftArm((float) -Math.PI / 2, (float) (Math.PI/4), 0);
        else
            if(getTick()<65)
                transformer.rotateRightArm((float) -Math.PI / 2, 0, 0).rotateLeftArm((float) -Math.PI / 2, (float) (Math.PI/4-Math.PI* (getTick()-60) / 5F), 0);
            else
                transformer.rotateRightArm((float) -Math.PI / 2, 0, 0).rotateLeftArm((float) -Math.PI / 2, (float)(-Math.PI*3/4), 0);

    }
}
