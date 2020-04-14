package com.grillo78.beycraft.items.render;

import com.grillo78.beycraft.items.ItemDualLauncher;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class LauncherItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

    @Override
    public void render(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        super.render(stack, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
        matrixStack.push();
        if(!(ItemModels.MODELS.containsKey(stack.getItem().getTranslationKey() + "_body") && ItemModels.MODELS.get(stack.getItem().getTranslationKey() + "_body") != null)) {
            ItemModels.MODELS.put(stack.getItem().getTranslationKey() + "_body", Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft", "launchers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/launcher_body")));
            ItemModels.MODELS.put(stack.getItem().getTranslationKey() + "_grab_part", Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft", "launchers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/grab_part")));
            if (stack.getItem() instanceof ItemDualLauncher) {
                ItemModels.MODELS.put(stack.getItem().getTranslationKey() + "_lever", Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft", "launchers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/launcher_lever")));
            }
        }
        IBakedModel model = ItemModels.MODELS.get(stack.getItem().getTranslationKey() + "_body");
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getEntitySolid(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
        for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
            vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 1, combinedOverlayIn, true);
        }
        model = ItemModels.MODELS.get(stack.getItem().getTranslationKey() + "_grab_part");
        for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
            vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 1, combinedOverlayIn, true);
        }
        if (stack.getItem() instanceof ItemDualLauncher) {
            model = ItemModels.MODELS.get(stack.getItem().getTranslationKey() + "_lever");
            for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
                vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 1, combinedOverlayIn, true);
            }
        }
        matrixStack.pop();
    }
}