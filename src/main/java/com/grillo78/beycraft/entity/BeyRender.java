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
    public ResourceLocation getEntityTexture(EntityBey entity) {
        return null;
    }

    public float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Override
    public void render(EntityBey entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (this.renderManager.pointedEntity == entity && !Minecraft.getInstance().player.isSpectator()
                && Minecraft.isGuiEnabled()) {
            matrixStack.push();
            matrixStack.translate(0, 0.1F, 0);
            renderName(entity, new StringTextComponent(entity.getPlayerName()), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.translate(0, -0.25F, 0);
            renderName(entity, new StringTextComponent("Health: "+round(entity.getHealth()*100/entity.getMaxHealth(),2)+"%"), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.pop();
        }
        matrixStack.push();
//        Minecraft.getInstance().gameRenderer.loadShader(new ResourceLocation("shaders/post/entity_outline.json"));
        if (!entity.isDescending() && !entity.isStoped()) {
            Matrix4f matrix4f1 = matrixStack.getLast().getMatrix();
            for (int i = 0; i < entity.getPoints().length; i++) {
                if (entity.getPoints()[i] != null) {
                    if (i != entity.getPoints().length - 1) {
                        float x = (float) (entity.getPoints()[i].x - entity.getPositionVec().x);
                        float y = (float) (entity.getPoints()[i].y - entity.getPositionVec().y);
                        float z = (float) (entity.getPoints()[i].z - entity.getPositionVec().z);
                        float x1 = (float) (entity.getPoints()[i + 1].x - entity.getPositionVec().x);
                        float y1 = (float) (entity.getPoints()[i + 1].y - entity.getPositionVec().y);
                        float z1 = (float) (entity.getPoints()[i + 1].z - entity.getPositionVec().z);
                        IVertexBuilder wr2 = bufferIn.getBuffer(CustomRenderType.THINLINE);
                        wr2.pos(matrix4f1, x, y, z).color(255, 255, 0, 255).endVertex();
                        wr2.pos(matrix4f1, x1, y1, z1).color(255, 255, 0, 255).endVertex();
                        IVertexBuilder wr = bufferIn.getBuffer(CustomRenderType.THICKLINE);
                        wr.pos(matrix4f1, x, y, z).color(255, 255, 0, 80).endVertex();
                        wr.pos(matrix4f1, x1, y1, z1).color(255, 255, 0, 80).endVertex();
                    } else {
                        float x = (float) (entity.getPoints()[i].x - entity.getPositionVec().x);
                        float y = (float) (entity.getPoints()[i].y - entity.getPositionVec().y);
                        float z = (float) (entity.getPoints()[i].z - entity.getPositionVec().z);
                        IVertexBuilder wr2 = bufferIn.getBuffer(CustomRenderType.THINLINE);
                        wr2.pos(matrix4f1, x, y, z).color(255, 255, 0, 255).endVertex();
                        wr2.pos(matrix4f1, 0, 0, 0).color(255, 255, 0, 255).endVertex();
                        IVertexBuilder wr = bufferIn.getBuffer(CustomRenderType.THICKLINE);
                        wr.pos(matrix4f1, x, y, z).color(255, 255, 0, 80).endVertex();
                        wr.pos(matrix4f1, 0, 0, 0).color(255, 255, 0, 80).endVertex();
                    }
                }
            }
        }
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        matrixStack.rotate(new Quaternion(90, 0, 0, true));
        matrixStack.translate(0, 0, -0.30);
        if (entity.getRadius() != 0 && entity.getRotationSpeed() > 2) {
            if(((ItemBeyDriver)entity.getDriver().getItem()).getType(entity.getDriver()) == BeyTypes.ATTACK){
                matrixStack.rotate(
                        new Quaternion(new Vector3f((float) -entity.getLookVec().x * -entity.getRotationDirection(), 0,
                                (float) -entity.getLookVec().z * entity.getRotationDirection()), -30, true));
            } else {
                matrixStack.rotate(
                        new Quaternion(new Vector3f((float) -entity.getLookVec().x * entity.getRotationDirection(), 0,
                                (float) -entity.getLookVec().z * entity.getRotationDirection()), -30, true));
            }
        }
        matrixStack.rotate(new Quaternion(0, 0, -entity.angle*1.5f, true));
        if (entity.getRotationSpeed() < 1) {
            matrixStack.rotate(
                    new Quaternion(new Vector3f((float) entity.getLookVec().x, (float) entity.getLookVec().z, 0),
                            40 * ((1 - entity.getRotationSpeed())), true));
        }
        Minecraft.getInstance().getItemRenderer().renderItem(entity.getLayer(), TransformType.FIXED, Minecraft.getInstance().getRenderManager().getPackedLight(entity,partialTicks),
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        if(entity.getRotationDirection()==1){
            matrixStack.rotate(new Quaternion(0, 0,
                    entity.getRotationDirection() * -entity.getHealth() + 85, true));
        } else {
            matrixStack.rotate(new Quaternion(0, 0,
                    entity.getRotationDirection() * -entity.getHealth() +11, true));
        }
        matrixStack.translate(0, 0, 0.078);
        Minecraft.getInstance().getItemRenderer().renderItem(entity.getDisc(), TransformType.FIXED, Minecraft.getInstance().getRenderManager().getPackedLight(entity,partialTicks),
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        matrixStack.translate(0, 0, 0.15);
        Minecraft.getInstance().getItemRenderer().renderItem(entity.getDriver(), TransformType.FIXED, Minecraft.getInstance().getRenderManager().getPackedLight(entity,partialTicks),
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        matrixStack.pop();
    }


}
