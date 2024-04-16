package grillo78.beycraft.mixin;

import com.mojang.authlib.GameProfile;
import grillo78.beycraft.common.capability.entity.Blader;
import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    public ClientPlayerEntityMixin(World pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void onDrop(CallbackInfoReturnable ci){
        Blader blader = getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null);
        if (blader.isLaunching())
            ci.cancel();
    }
}
