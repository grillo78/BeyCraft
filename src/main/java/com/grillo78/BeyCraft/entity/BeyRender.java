package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.BeyCraft;
import com.mojang.blaze3d.matrix.MatrixStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class BeyRender extends EntityRenderer<EntityBey> {


    protected BeyRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBey entity) {
        return null;
    }


    @Override
    public void render(EntityBey entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {

        if (this.renderManager.pointedEntity == entity && !Minecraft.getInstance().player.isSpectator()
                && Minecraft.isGuiEnabled()) {
            matrixStack.push();
            matrixStack.translate(0, 0.5F, 0);
            renderName(entity, entity.getLayer().getItem().getName().getFormattedText(), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.translate(0, -0.25F, 0);
            renderName(entity, entity.getDisk().getItem().getName().getFormattedText(), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.translate(0, -0.25F, 0);
            renderName(entity, entity.getDriver().getItem().getName().getFormattedText(), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.translate(0, -0.25F, 0);
            renderName(entity, "Speed:" + entity.getRotationSpeed(), matrixStack, bufferIn, packedLightIn);
            matrixStack.pop();
        }
        matrixStack.push();
        if (!entity.isStoped()) {
            Vec3d[] points = entity.getPoints();
            IVertexBuilder wr = bufferIn.getBuffer(RenderType.getLines());
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
                        wr.pos(matrix4f1, x, y, z).color(255, 255, 0, 255).endVertex();
                        wr.pos(matrix4f1, x1, y1, z1).color(255, 255, 0, 255).endVertex();
                    } else {
                        float x = (float) (entity.getPoints()[i].x - entity.getPositionVec().x);
                        float y = (float) (entity.getPoints()[i].y - entity.getPositionVec().y);
                        float z = (float) (entity.getPoints()[i].z - entity.getPositionVec().z);
                        wr.pos(matrix4f1, x, y, z).color(255, 255, 0, 255).endVertex();
                        wr.pos(matrix4f1, 0, 0, 0).color(255, 255, 0, 255).endVertex();
                    }
                }
            }


        }
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        matrixStack.rotate(new Quaternion(90, 0, 0, true));
        matrixStack.translate(0, 0, -0.25);
        if (entity.getRadius() != 0 && entity.getRotationSpeed() > 2) {
            matrixStack.rotate(
                    new Quaternion(new Vector3f((float) entity.getLookVec().x * entity.getRotationDirection(), 0,
                            (float) entity.getLookVec().z * entity.getRotationDirection()), -30, true));
        }
        matrixStack.rotate(new Quaternion(0, 0, entity.angle * 2, true));
        if (entity.getRotationSpeed() < 2) {
            matrixStack.rotate(
                    new Quaternion(new Vector3f((float) entity.getLookVec().x, (float) entity.getLookVec().z, 0),
                            40 * ((2 - entity.getRotationSpeed()) / (2)), true));
        }
        Minecraft.getInstance().getItemRenderer().renderItem(entity.getLayer(), TransformType.FIXED, packedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        matrixStack.rotate(new Quaternion(0, 0,
                entity.getRotationDirection() * -entity.getHealth() + entity.getRotationDirection() + 85, true));
        matrixStack.translate(0, 0, 0.078);
        Minecraft.getInstance().getItemRenderer().renderItem(entity.getDisk(), TransformType.FIXED, packedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        matrixStack.translate(0, 0, 0.15);
        Minecraft.getInstance().getItemRenderer().renderItem(entity.getDriver(), TransformType.FIXED, packedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        matrixStack.pop();
    }


}
