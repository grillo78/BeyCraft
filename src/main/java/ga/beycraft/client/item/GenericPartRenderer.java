package ga.beycraft.client.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import ga.beycraft.client.util.BeyPartModel;
import ga.beycraft.common.item.DiscFrameItem;
import ga.beycraft.common.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.opengl.GL11;

public class GenericPartRenderer extends ItemStackTileEntityRenderer {
    private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);

    @Override
    public void renderByItem(ItemStack stack, TransformType transformType, MatrixStack matrixStack,
                             IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
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
                sceneLayer = layer.addRenderObject(ModItems.ItemCreator.models.get(stack.getItem()));
                break;
            case FIRST_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
                vector4f = new Vector4f(0, 0, 0, 1 / 16);
                vector4f.transform(matrix);
                pos = cameraPos.add(vector4f.x(), vector4f.y() + 0.2, vector4f.z());
                model = new BeyPartModel(modelView, pos, ModItems.ItemCreator.models.get(stack.getItem()), 20, 180, 0, 0.5F, 0.5F, 0.5F);
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
                model = new BeyPartModel(modelView, pos, ModItems.ItemCreator.models.get(stack.getItem()), 0, 0, 0, 1, 1, 1);
                sceneLayer = model.getSceneLayer();
                BeyPartModel.worldModels.add(model);
                break;
        }
        if (model != null) {
            BeyPartModel finalModel = model;
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
                Item frameItem = cap.getStackInSlot(0).getItem();
                if (ModItems.ItemCreator.models.containsKey(frameItem) && stack.getItem() instanceof DiscFrameItem) {
                    RenderObject sceneFrame = finalModel.addChild(ModItems.ItemCreator.models.get(frameItem));
                    sceneFrame.transform.rotate(0,((DiscFrameItem)stack.getItem()).getFrameRotation(),0);
                }
            });
        } else {
            GL11.glRotatef(50, 1, 0, 0);
            GL11.glTranslated(0, -0.5, 0);
            GL11.glScaled(2, 2, 2);
            vector4f = new Vector4f(0, 0, 0, 16);
            vector4f.transform(matrix);
            pos = cameraPos.add(vector4f.x(), vector4f.y(), vector4f.z());

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            sceneLayer.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
            sceneLayer.forceTransformUpdate();
            RenderManager.render(layer, RenderMode.USE_FFP_MATS);
            sceneLayer.remove();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
        matrixStack.popPose();
    }
}