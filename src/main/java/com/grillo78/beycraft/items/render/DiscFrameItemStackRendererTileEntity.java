package com.grillo78.beycraft.items.render;

import com.grillo78.beycraft.items.ItemBeyDiscFrame;
import com.mojang.blaze3d.matrix.MatrixStack;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class DiscFrameItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

    @Override
    public void func_239207_a_(ItemStack stack,TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn,
                       int combinedOverlayIn) {
        matrixStack.push();
        if(!ItemModels.MODELS.containsKey(stack.getItem().getTranslationKey()) || ItemModels.MODELS.get(stack.getItem().getTranslationKey()) == null) {
            ItemModels.MODELS.put(stack.getItem().getTranslationKey(),Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft", "discsframe/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "")));

        }
        IBakedModel model = ItemModels.MODELS.get(stack.getItem().getTranslationKey());
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getEntityTranslucent(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
        for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
            vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 1, combinedOverlayIn, true);
        }
        if (stack.hasTag()) {
            matrixStack.scale(2F, 2F, 2F);
            matrixStack.rotate(new Quaternion(new Vector3f(0, ((ItemBeyDiscFrame)stack.getItem()).getFrameRotation(), 0), -15, true));
            matrixStack.translate(0F, 0.01F, 0.25F);
            Minecraft.getInstance().getItemRenderer().renderItem(ItemStack.read((CompoundNBT) stack.getTag().get("frame")),
                    TransformType.FIRST_PERSON_LEFT_HAND, 0, OverlayTexture.NO_OVERLAY, matrixStack, buffer);

        }

        matrixStack.pop();
        super.func_239207_a_(stack, transformType, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }
}
