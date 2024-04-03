package grillo78.beycraft.client.entity.animator;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.common.capability.Parkourability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;

public abstract class BeycraftAnimator extends Animator {
    protected static float MAX_TICKS = 70;

    protected HandSide hand;

    public BeycraftAnimator(HandSide hand) {
        this.hand = hand;
    }

    @Override
    public boolean shouldRemoved(PlayerEntity playerEntity, Parkourability parkourability) {
        return getTick() >= MAX_TICKS;
    }
}
