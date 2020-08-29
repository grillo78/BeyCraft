package com.grillo78.beycraft.mixin;

import com.grillo78.beycraft.items.ItemLauncher;
import com.grillo78.beycraft.items.ItemLauncherHandle;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingRenderer.class)
public abstract class MixinPlayerRenderer implements IEntityRenderer {

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getInstance()Lnet/minecraft/client/Minecraft;"))
    public void onRender(LivingEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo ci) {
        if (entityIn instanceof AbstractClientPlayerEntity){
            PlayerEntity player = (PlayerEntity) entityIn;
            if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemLauncher
                    || player.getHeldItem(Hand.OFF_HAND).getItem() instanceof ItemLauncher) {
                PlayerModel model = (PlayerModel) this.getEntityModel();
                model.bipedLeftArm.rotateAngleX = (float) Math.toRadians(player.rotationPitch - 90);
                model.bipedRightArm.rotateAngleX = (float) Math.toRadians(player.rotationPitch - 90);

                model.bipedLeftArmwear.rotateAngleX = (float) Math.toRadians(player.rotationPitch - 90);
                model.bipedRightArmwear.rotateAngleX = (float) Math.toRadians(player.rotationPitch - 90);

                model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(25);
                model.bipedRightArm.rotateAngleY = (float) Math.toRadians(-25);
                model.bipedRightArm.rotateAngleZ = (float) Math
                        .toRadians(25 * (1 - ((90 - (-player.rotationPitch)) / 90)));
                model.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(25 * (1 - ((90 - player.rotationPitch) / 90)));

                model.bipedLeftArmwear.rotateAngleY = (float) Math.toRadians(25);
                model.bipedRightArmwear.rotateAngleY = (float) Math.toRadians(-25);
                model.bipedRightArmwear.rotateAngleZ = (float) Math
                        .toRadians(25 * (1 - ((90 - (-player.rotationPitch)) / 90)));
                model.bipedLeftArmwear.rotateAngleZ = (float) Math
                        .toRadians(25 * (1 - ((90 - player.rotationPitch) / 90)));

                if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemLauncher) {
                    if (player.getHeldItem(Hand.MAIN_HAND).hasTag()
                            && (player.getHeldItem(Hand.MAIN_HAND).getTag().contains("handle")
                            && ItemStack.read(player.getHeldItem(Hand.MAIN_HAND).getTag().getCompound("handle"))
                            .getItem() instanceof ItemLauncherHandle)) {
                        if (player.getPrimaryHand() == HandSide.RIGHT) {
                            model.bipedRightArm.rotateAngleY = (float) Math.toRadians(0);
                            model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(0);
                            model.bipedRightArmwear.rotateAngleY = (float) Math.toRadians(0);
                            model.bipedRightArmwear.rotateAngleZ = (float) Math.toRadians(0);
                        } else {
                            model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(0);
                            model.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(0);
                            model.bipedLeftArmwear.rotateAngleY = (float) Math.toRadians(0);
                            model.bipedLeftArmwear.rotateAngleZ = (float) Math.toRadians(0);
                        }
                    }
                    if (player.getCooldownTracker().hasCooldown(player.getHeldItem(Hand.MAIN_HAND).getItem())) {
                        if (player.getPrimaryHand() == HandSide.RIGHT) {
                            model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(-25);
                            model.bipedLeftArm.rotateAngleZ = (float) Math
                                    .toRadians(-25 * (1 - ((90 - player.rotationPitch) / 90)));
                            model.bipedLeftArmwear.rotateAngleY = (float) Math.toRadians(-25);
                            model.bipedLeftArmwear.rotateAngleZ = (float) Math
                                    .toRadians(-25 * (1 - ((90 - player.rotationPitch) / 90)));
                        } else {
                            model.bipedRightArm.rotateAngleY = (float) Math.toRadians(25);
                            model.bipedRightArm.rotateAngleZ = (float) Math
                                    .toRadians(-25 * (1 - ((90 - (-player.rotationPitch)) / 90)));
                            model.bipedRightArmwear.rotateAngleY = (float) Math.toRadians(25);
                            model.bipedRightArmwear.rotateAngleZ = (float) Math
                                    .toRadians(-25 * (1 - ((90 - (-player.rotationPitch)) / 90)));
                        }
                    }
                } else {
                    if (player.getHeldItem(Hand.OFF_HAND).hasTag()
                            && (player.getHeldItem(Hand.OFF_HAND).getTag().contains("handle")
                            && ItemStack.read(player.getHeldItem(Hand.OFF_HAND).getTag().getCompound("handle"))
                            .getItem() instanceof ItemLauncherHandle)) {
                        if (player.getPrimaryHand() == HandSide.RIGHT) {
                            model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(0);
                            model.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(0);
                            model.bipedLeftArmwear.rotateAngleY = (float) Math.toRadians(0);
                            model.bipedLeftArmwear.rotateAngleZ = (float) Math.toRadians(0);
                        } else {
                            model.bipedRightArm.rotateAngleY = (float) Math.toRadians(0);
                            model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(0);
                            model.bipedRightArmwear.rotateAngleY = (float) Math.toRadians(0);
                            model.bipedRightArmwear.rotateAngleZ = (float) Math.toRadians(0);
                        }
                    }
                    if (player.getCooldownTracker().hasCooldown(player.getHeldItem(Hand.OFF_HAND).getItem())) {
                        if (player.getPrimaryHand() == HandSide.RIGHT) {
                            model.bipedRightArm.rotateAngleY = (float) Math.toRadians(25);
                            model.bipedRightArm.rotateAngleZ = (float) Math
                                    .toRadians(-25 * (1 - ((90 - (-player.rotationPitch)) / 90)));
                            model.bipedRightArmwear.rotateAngleY = (float) Math.toRadians(25);
                            model.bipedRightArmwear.rotateAngleZ = (float) Math
                                    .toRadians(-25 * (1 - ((90 - (-player.rotationPitch)) / 90)));
                        } else {
                            model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(-25);
                            model.bipedLeftArm.rotateAngleZ = (float) Math
                                    .toRadians(-25 * (1 - ((90 - player.rotationPitch) / 90)));
                            model.bipedLeftArmwear.rotateAngleY = (float) Math.toRadians(-25);
                            model.bipedLeftArmwear.rotateAngleZ = (float) Math
                                    .toRadians(-25 * (1 - ((90 - player.rotationPitch) / 90)));
                        }
                    }
                }
            }
        }
    }
}
