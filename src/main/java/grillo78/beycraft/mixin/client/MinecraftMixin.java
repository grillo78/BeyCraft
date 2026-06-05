package grillo78.beycraft.mixin.client;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.client.render.MeshRegistry;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "onResourceLoadFinished", at = @At("RETURN"))
    public void onCloseEnd(CallbackInfo ci){
        MeshRegistry.registerAll();
    }
}
