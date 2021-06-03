package com.grillo78.beycraft.items.render;

import com.grillo78.beycraft.entity.BeyRender;
import com.grillo78.beycraft.util.BeyPartModel;
import com.grillo78.beycraft.util.CachedStacks;
import com.grillo78.beycraft.util.ItemCreator;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BeyItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

    private Random random = new Random();
    private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);
    private HashMap<CompoundNBT, ItemStack> stacks;


    public BeyItemStackRendererTileEntity() {
        stacks = CachedStacks.INSTANCE.getStacks();
    }

    @Override
    public void renderByItem(ItemStack stack, TransformType transformType, MatrixStack matrixStack,
                             IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        super.renderByItem(stack, transformType, matrixStack, buffer, combinedLightIn, combinedOverlayIn);

        matrixStack.pushPose();
        net.minecraft.util.math.vector.Matrix4f matrix = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
        net.minecraft.util.math.vector.Matrix4f modelView = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
        Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrix.invert();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        vector4f.transform(matrix);
        Vector3d pos = cameraPos.add(vector4f.x(), vector4f.y(), vector4f.z());
        RenderObject sceneLayer;
        BeyPartModel model = null;
        switch (transformType) {
            case GUI:
                sceneLayer = layer.addRenderObject(ItemCreator.models.get(stack.getItem()));
                break;
            case FIRST_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
                vector4f = new Vector4f(0, 0, 0, 1 / 16);
                vector4f.transform(matrix);
                pos = cameraPos.add(vector4f.x(), vector4f.y() + 0.2, vector4f.z());
                model = new BeyPartModel(modelView, pos, ItemCreator.models.get(stack.getItem()), 20, 180, 0, 0.5F, 0.5F, 0.5F);
                sceneLayer = model.getSceneLayer();
                BeyPartModel.handModels.add(model);
                break;
            case GROUND:
            case THIRD_PERSON_RIGHT_HAND:
            case THIRD_PERSON_LEFT_HAND:
                vector4f = new Vector4f(0, 0, 0, 1 / 16);
                vector4f.transform(matrix);
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                matrixStack.translate(0.5F, 0.5F, 0.5F);
                modelView = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
                pos = cameraPos.add(vector4f.x(), vector4f.y(), vector4f.z());
            default:
                model = new BeyPartModel(modelView, pos, ItemCreator.models.get(stack.getItem()), 0, 0, 0, 1, 1, 1);
                sceneLayer = model.getSceneLayer();
                BeyPartModel.worldModels.add(model);
                break;
        }
        if (stack.hasTag() && stack.getTag().contains("chip")) {

            if (!stacks.containsKey(stack.getTag().get("chip"))) {
                stacks.put((CompoundNBT) stack.getTag().get("chip"),
                        ItemStack.of((CompoundNBT) stack.getTag().get("chip")));
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(stacks.get(stack.getTag().get("chip")),
                    TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);


        }
        if (stack.hasTag() && stack.getTag().contains("weight")) {
            matrixStack.mulPose(new Quaternion(new Vector3f(0, 0, 1), 180, true));
            matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), 45, true));
            matrixStack.translate(0.18, -0.05, 0.07);
            if (!stacks.containsKey(stack.getTag().get("weight"))) {
                stacks.put((CompoundNBT) stack.getTag().get("weight"),
                        ItemStack.of((CompoundNBT) stack.getTag().get("weight")));
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(stacks.get(stack.getTag().get("weight")),
                    TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);

        }
        matrixStack.popPose();
        matrixStack.pushPose();
        if (stack.hasTag() && stack.getTag().contains("isEntity") && !stack.getTag().getBoolean("isEntity")) {
            if (model != null) {
                Item discItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("disc")).getString("id")));
                if (ItemCreator.models.containsKey(discItem)) {
                    RenderObject sceneDisc = model.addChild(ItemCreator.models.get(discItem));
                    Item driverItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("driver")).getString("id")));
                    if (ItemCreator.models.containsKey(discItem)) {
                        RenderObject sceneDriver = model.addChild(ItemCreator.models.get(driverItem));
                    }
                }
            } else {
                GL11.glRotatef(45, 1, 0, 0);
                vector4f = new Vector4f(0, 0, 0, 16);
                vector4f.transform(matrix);
                pos = cameraPos.add(vector4f.x(), vector4f.y(), vector4f.z());

                RenderObject sceneDriver = null;
                RenderObject sceneDisc = null;
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                sceneLayer.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);

                Item discItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("disc")).getString("id")));
                if (ItemCreator.models.containsKey(discItem)) {
                    sceneDisc = sceneLayer.addChild(ItemCreator.models.get(discItem));
                    Item driverItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("driver")).getString("id")));
                    if (ItemCreator.models.containsKey(discItem)) {
                        sceneDriver = sceneLayer.addChild(ItemCreator.models.get(driverItem));
                    }
                }
                sceneLayer.forceTransformUpdate();
                RenderManager.render(layer, RenderMode.USE_FFP_MATS);
                if (sceneDisc != null) {
                    if (sceneDriver != null) {
                        sceneDriver.remove();
                    }
                    sceneDisc.remove();
                }
                layer.removeRenderObject(sceneLayer);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            }
        }
        matrixStack.popPose();
    }

}
