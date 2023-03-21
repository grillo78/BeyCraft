package com.beycraft.client.entity.animator;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PlayerModelRotator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.capability.Parkourability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;

public class NightmareShootAnimator extends BeycraftAnimator {

    public NightmareShootAnimator(HandSide hand) {
        super(hand);
    }

    @Override
    public void animatePost(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
        if (hand == HandSide.RIGHT) {
            if(getTick()<10) {
                transformer
                        .rotateRightArm((float) -(Math.PI / 2) * (getTick() / 10F), 0, 0)
                        .rotateLeftArm((float) -(Math.PI / 2) * (getTick() / 10F), (float) (Math.PI / 4) * (getTick() / 10F), 0);
            }
            else if(getTick()<60) {
                transformer
                        .rotateRightArm((float) -Math.PI / 2, 0, 0)
                        .rotateLeftArm((float) -Math.PI / 2 + (float) (-2*Math.PI) * ((getTick() - 10) / 50F), (float) Math.PI / 4, 0);
            }
            else
            if(getTick()<65)
                transformer
                        .rotateRightArm((float) -Math.PI / 2, 0, 0)
                        .rotateLeftArm((float) -Math.PI / 2, (float) (Math.PI/4-Math.PI* (getTick()-60) / 5F), 0);
            else
                transformer
                        .rotateRightArm((float) -Math.PI / 2, 0, 0)
                        .rotateLeftArm((float) -Math.PI / 2, (float)(-Math.PI*3/4), 0);
        } else {

            if (getTick() < 10) {
                transformer.rotateLeftArm((float) -(Math.PI / 2) * (getTick() / 10F), 0, 0).rotateRightArm((float) -(Math.PI / 2) * (getTick() / 10F), (float) (-Math.PI / 4 * (getTick() / 10F)), 0);
            } else if (getTick() < 60)
                transformer.rotateLeftArm((float) -Math.PI / 2, 0, 0).rotateRightArm((float) -Math.PI / 2 + (float) (-2*Math.PI) * ((getTick() - 10) / 50F), -(float) Math.PI / 4, 0);
            else if (getTick() < 65)
                transformer.rotateLeftArm((float) -Math.PI / 2, 0, 0).rotateRightArm((float) -Math.PI / 2, (float) (-Math.PI / 4 + Math.PI * (getTick() - 60) / 5F), 0);
            else
                transformer.rotateLeftArm((float) -Math.PI / 2, 0, 0).rotateRightArm((float) -Math.PI / 2, (float) (+Math.PI * 3 / 4), 0);
        }
    }
}
