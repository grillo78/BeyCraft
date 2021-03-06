package com.grillo78.beycraft.entity;

import com.grillo78.beycraft.util.CustomRenderType;
import com.grillo78.beycraft.util.ItemCreator;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderEffect;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.math.Matrix4f;
import friedrichlp.renderlib.model.ModelLoaderProperty;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.math.BigDecimal;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class BeyRender extends EntityRenderer<EntityBey> {
    private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);
    private static ArrayList<Runnable> runnables = new ArrayList<>();

    public static ArrayList<Runnable> getRunnables() {
        return runnables;
    }

    public BeyRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBey entity) {
        return null;
    }

    @Override
    public void render(EntityBey entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {
        net.minecraft.util.math.vector.Matrix4f matrix = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
        Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrix.invert();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        vector4f.transform(matrix);
        Vector3d pos = cameraPos.add(-vector4f.x(), -vector4f.y(), -vector4f.z());
        runnables.add(() -> {
            RenderLibRegistry.Compatibility.MODEL_VIEW_PROVIDER = () -> {
                FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
                matrixStack.last().pose().store(floatBuffer);

                return new Matrix4f(floatBuffer);
            };
            RenderObject sceneDriver = layer.addRenderObject(ItemCreator.models.get(entity.getDriver().getItem()));
            RenderObject sceneDisc = sceneDriver.addChild(ItemCreator.models.get(entity.getDisc().getItem()));
            RenderObject sceneLayer = sceneDisc.addChild(ItemCreator.models.get(entity.getLayer().getItem()));

            sceneDriver.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
            sceneDriver.transform.scale(0.5F, 0.5F, 0.5F);
            Vector3d vector = entity.getLookAngle().yRot(90);
            if (entity.getRotationSpeed() > 1) {
                sceneDriver.transform.rotate((float) vector.x * 30 * entity.getRadius() * entity.getRotationDirection(), -entity.angle * 1.5f, (float) vector.z * 30 * entity.getRadius() * entity.getRotationDirection());
            } else {
                sceneDriver.transform.rotate((float) vector.x * 30 * entity.getRotationDirection(), -entity.angle * 1.5f, (float) vector.z * 30 * entity.getRotationDirection());
            }
            sceneDriver.forceTransformUpdate();

            RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);
            sceneLayer.remove();
            sceneDisc.remove();
            layer.removeRenderObject(sceneDriver);
        });
        if (this.entityRenderDispatcher.crosshairPickEntity == entity && !Minecraft.getInstance().player.isSpectator()
                && Minecraft.renderNames()) {
            matrixStack.pushPose();
            matrixStack.translate(0, 0.1F, 0);
            renderNameTag(entity, new StringTextComponent(entity.getPlayerName()), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.translate(0, -0.25F, 0);
            renderNameTag(entity, new StringTextComponent("Health: " + round(entity.getHealth() * 100 / entity.getMaxHealth(), 2) + "%"), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.popPose();
        }
        if (!entity.isDescending() && !entity.isStoped()) {
            net.minecraft.util.math.vector.Matrix4f matrix4f1 = matrixStack.last().pose();
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
//        matrixStack.scale(0.5F, 0.5F, 0.5F);
//        matrixStack.mulPose(new Quaternion(90, 0, 0, true));
//        matrixStack.translate(0, 0, -0.30);
//        matrixStack.mulPose(new Quaternion(0, 0, -entity.angle*1.5f, true));
//        if (entity.getRotationSpeed() < 1) {
//            matrixStack.mulPose(
//                    new Quaternion(new Vector3f((float) entity.getLookAngle().x, (float) entity.getLookAngle().z, 0),
//                            40 * ((1 - entity.getRotationSpeed())), true));
//        }
//        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getLayer(), TransformType.FIXED, Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(entity,partialTicks),
//                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
//        if(entity.getRotationDirection()==1){
//            matrixStack.mulPose(new Quaternion(0, 0,
//                    entity.getRotationDirection() * -entity.getHealth() + 85, true));
//        } else {
//            matrixStack.mulPose(new Quaternion(0, 0,
//                    entity.getRotationDirection() * -entity.getHealth() +11, true));
//        }
//        matrixStack.translate(0, 0, 0.078);
//        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getDisc(), TransformType.FIXED, Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(entity,partialTicks),
//                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
//        matrixStack.translate(0, 0, 0.15);
//        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getDriver(), TransformType.FIXED, Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(entity,partialTicks),
//                OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
//        matrixStack.popPose();
    }

    public float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
