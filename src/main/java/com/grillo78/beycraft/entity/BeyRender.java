package com.grillo78.beycraft.entity;

import com.grillo78.beycraft.items.ItemBeyDriver;
import com.grillo78.beycraft.util.BeyTypes;
import com.grillo78.beycraft.util.CustomRenderType;
import com.mojang.blaze3d.matrix.MatrixStack;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;

import java.math.BigDecimal;

public class BeyRender extends EntityRenderer<EntityBey> {


    protected BeyRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBey entity) {
        return null;
    }

    public float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Override
    public void render(EntityBey entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (this.entityRenderDispatcher.crosshairPickEntity == entity && !Minecraft.getInstance().player.isSpectator()
                && Minecraft.renderNames()) {
            matrixStack.pushPose();
            matrixStack.translate(0, 0.1F, 0);
            renderNameTag(entity, new StringTextComponent(entity.getPlayerName()), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.translate(0, -0.25F, 0);
            renderNameTag(entity, new StringTextComponent("Health: "+round(entity.getHealth()*100/entity.getMaxHealth(),2)+"%"), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.popPose();
        }
        matrixStack.pushPose();
//        Minecraft.getInstance().gameRenderer.loadShader(new ResourceLocation("shaders/post/entity_outline.json"));
        if (!entity.isDescending() && !entity.isStoped()) {
            Matrix4f matrix4f1 = matrixStack.last().pose();
            for (int i = 0; i < entity.getPoints().length; i++) {
                if (entity.getPoints()[i] != null) {
                    if (i != entity.getPoints().length - 1) {
                        float x = (float) (entity.getPoints()[i].x - entity.position().x);
                        float y = (float) (entity.getPoints()[i].y - entity.position().y);
                        float z = (float) (entity.getPoints()[i].z - entity.position().z);
                        float x1 = (float) (entity.getPoints()[i + 1].x - entity.position().x);
                        float y1 = (float) (entity.getPoints()[i + 1].y - entity.position().y);
                        float z1 = (float) (entity.getPoints()[i + 1].z - entity.position().z);
                        IVertexBuilder wr2 = bufferIn.getBuffer(CustomRenderType.THINLINE);
                        wr2.vertex(matrix4f1, x, y, z).color(255, 255, 0, 255).endVertex();
                        wr2.vertex(matrix4f1, x1, y1, z1).color(255, 255, 0, 255).endVertex();
                        IVertexBuilder wr = bufferIn.getBuffer(CustomRenderType.THICKLINE);
                        wr.vertex(matrix4f1, x, y, z).color(255, 255, 0, 80).endVertex();
                        wr.vertex(matrix4f1, x1, y1, z1).color(255, 255, 0, 80).endVertex();
                    } else {
                        float x = (float) (entity.getPoints()[i].x - entity.position().x);
                        float y = (float) (entity.getPoints()[i].y - entity.position().y);
                        float z = (float) (entity.getPoints()[i].z - entity.position().z);
                        IVertexBuilder wr2 = bufferIn.getBuffer(CustomRenderType.THINLINE);
                        wr2.vertex(matrix4f1, x, y, z).color(255, 255, 0, 255).endVertex();
                        wr2.vertex(matrix4f1, 0, 0, 0).color(255, 255, 0, 255).endVertex();
                        IVertexBuilder wr = bufferIn.getBuffer(CustomRenderType.THICKLINE);
                        wr.vertex(matrix4f1, x, y, z).color(255, 255, 0, 80).endVertex();
                        wr.vertex(matrix4f1, 0, 0, 0).color(255, 255, 0, 80).endVertex();
                    }
                }
            }
        }
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        matrixStack.mulPose(new Quaternion(90, 0, 0, true));
        matrixStack.translate(0, 0, -0.30);
        if (entity.getRadius() != 0 && entity.getRotationSpeed() > 2) {
            if(((ItemBeyDriver)entity.getDriver().getItem()).getType(entity.getDriver()) == BeyTypes.ATTACK){
                matrixStack.mulPose(
                        new Quaternion(new Vector3f((float) -entity.getLookAngle().x * -entity.getRotationDirection(), 0,
                                (float) -entity.getLookAngle().z * entity.getRotationDirection()), -30, true));
            } else {
                matrixStack.mulPose(
                        new Quaternion(new Vector3f((float) -entity.getLookAngle().x * entity.getRotationDirection(), 0,
                                (float) -entity.getLookAngle().z * entity.getRotationDirection()), -30, true));
            }
        }
        matrixStack.mulPose(new Quaternion(0, 0, -entity.angle*1.5f, true));
        if (entity.getRotationSpeed() < 1) {
            matrixStack.mulPose(
                    new Quaternion(new Vector3f((float) entity.getLookAngle().x, (float) entity.getLookAngle().z, 0),
                            40 * ((1 - entity.getRotationSpeed())), true));
        }
        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getLayer(), TransformType.FIXED, Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(entity,partialTicks),
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        if(entity.getRotationDirection()==1){
            matrixStack.mulPose(new Quaternion(0, 0,
                    entity.getRotationDirection() * -entity.getHealth() + 85, true));
        } else {
            matrixStack.mulPose(new Quaternion(0, 0,
                    entity.getRotationDirection() * -entity.getHealth() +11, true));
        }
        matrixStack.translate(0, 0, 0.078);
        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getDisc(), TransformType.FIXED, Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(entity,partialTicks),
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        matrixStack.translate(0, 0, 0.15);
        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getDriver(), TransformType.FIXED, Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(entity,partialTicks),
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        matrixStack.popPose();
    }


}
