package grillo78.beycraft.mixin;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.AnimatorList;
import grillo78.beycraft.client.entity.animator.BasicLaunchAnimator;
import grillo78.beycraft.client.entity.animator.HandSpinningLaunchAnimator;
import grillo78.beycraft.client.entity.animator.NightmareShootAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(AnimatorList.class)
public class AnimatorListMixin {

    @Shadow(remap = false) public static List<Class<? extends Animator>> ANIMATORS;

    @Inject(method = "<clinit>", remap = false, at = @At(value = "INVOKE", target = "Ljava/util/HashMap;<init>(I)V"))
    private static void clinitInjection(CallbackInfo ci){
            ANIMATORS = new ArrayList<>(ANIMATORS);
            ANIMATORS.add(BasicLaunchAnimator.class);
            ANIMATORS.add(NightmareShootAnimator.class);
            ANIMATORS.add(HandSpinningLaunchAnimator.class);
    }
}
