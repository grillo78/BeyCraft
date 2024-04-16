package grillo78.beycraft.client.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.common.capability.item.LauncherCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class LauncherRenderer extends ItemStackTileEntityRenderer {

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
                             IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.pushPose();
        stack.getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).ifPresent(cap -> {
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(Beycraft.MOD_ID,
                    "launchers/" + stack.getItem().getRegistryName().getPath() + "/launcher_body"));

            IVertexBuilder vertexBuilder = buffer
                    .getBuffer(RenderType.entityTranslucentCull(PlayerContainer.BLOCK_ATLAS));
            for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
                vertexBuilder.addVertexData(matrixStack.last(), quad, cap.getRed() / 255F, cap.getGreen() / 255F, cap.getBlue() / 255F, combinedLightIn, combinedOverlayIn, true);
            }
            model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(Beycraft.MOD_ID,
                    "launchers/" + stack.getItem().getRegistryName().getPath() + "/grab_part"));
            for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
                vertexBuilder.addVertexData(matrixStack.last(), quad, cap.getRed() / 255F, cap.getGreen() / 255F, cap.getBlue() / 255F, combinedLightIn, combinedOverlayIn, true);
            }

            if (transformType != ItemCameraTransforms.TransformType.GUI) {
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                matrixStack.pushPose();
                matrixStack.translate(0.4, 0.45, 0.325);
                matrixStack.scale(0.85F, 0.85F, 0.85F);
                itemRenderer.renderStatic(cap.getInventory().getStackInSlot(1), ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
                itemRenderer.renderStatic(cap.getInventory().getStackInSlot(2), ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
                matrixStack.popPose();

                matrixStack.pushPose();
                switch (transformType) {
                    case THIRD_PERSON_RIGHT_HAND:
                        matrixStack.translate(-0.015, -0.175, 0);
                        matrixStack.scale(1.75F, 1.75F, 1.75F);
                        break;
                    case THIRD_PERSON_LEFT_HAND:
                        matrixStack.translate(-0.015, -0.225, 0);
                        matrixStack.scale(1.75F, 1.75F, 1.75F);
                        break;
                    case FIRST_PERSON_RIGHT_HAND:
                    case FIRST_PERSON_LEFT_HAND:
                        matrixStack.translate(-0.015, -0.5, 0.2);
                        matrixStack.mulPose(new Quaternion(-20, 0, 0, true));
                        matrixStack.scale(1.75F, 1.75F, 1.75F);
                        break;
                    case GROUND:
                        matrixStack.translate(0, -0.2, 0);
                        matrixStack.scale(1.75F, 1.75F, 1.75F);
                        break;
                    case FIXED:
                        matrixStack.translate(-0.015, -0.225, 0);
                        break;
                }
                itemRenderer.renderStatic(cap.getInventory().getStackInSlot(0), transformType, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
                matrixStack.popPose();
            }
        });
        matrixStack.popPose();
    }
}
