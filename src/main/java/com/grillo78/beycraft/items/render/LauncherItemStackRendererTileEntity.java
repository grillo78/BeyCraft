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
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

public class LauncherItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

    @Override
    public void render(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        super.render(stack, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
        matrixStack.push();
        matrixStack.scale(1.5f,1.5f,1.5f);
        matrixStack.translate(0,-0.05,0);
        matrixStack.rotate(new Quaternion(new Vector3f(0, 1, 0), 90, true));
        if(!(ItemModels.MODELS.containsKey(stack.getItem().getTranslationKey() + "_body") && ItemModels.MODELS.get(stack.getItem().getTranslationKey() + "_body") != null)) {
            ItemModels.MODELS.put(stack.getItem().getTranslationKey() + "_body", Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft", "launchers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/launcher_body")));
            ItemModels.MODELS.put(stack.getItem().getTranslationKey() + "_grab_part", Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft", "launchers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/grab_part")));
            if (stack.getItem() instanceof ItemDualLauncher) {
                ItemModels.MODELS.put(stack.getItem().getTranslationKey() + "_lever", Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft", "launchers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/launcher_lever")));
            }
        }
        IBakedModel model = ItemModels.MODELS.get(stack.getItem().getTranslationKey() + "_body");
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getEntityTranslucentCull(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
        Direction[] values = ArrayUtils.addAll(Direction.values(), null);
        for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
            vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 1, combinedOverlayIn, true);
        }
        model = ItemModels.MODELS.get(stack.getItem().getTranslationKey() + "_grab_part");
        for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
            vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 1, combinedOverlayIn, true);
        }
        if (stack.getItem() instanceof ItemDualLauncher) {
            if(stack.hasTag() && stack.getTag().contains("rotation") && stack.getTag().getInt("rotation") == 1){
                matrixStack.translate(0,0,-0.1);
            }
            model = ItemModels.MODELS.get(stack.getItem().getTranslationKey() + "_lever");
            for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
                vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 1, combinedOverlayIn, true);
            }
        }
        matrixStack.pop();
        matrixStack.push();
        matrixStack.scale(1.5f,1.5f,1.5f);
        matrixStack.rotate(new Quaternion(new Vector3f(1, 0, 0), 90, true));
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
            Minecraft.getInstance().getItemRenderer().renderItem(h.getStackInSlot(0),TransformType.FIXED,combinedLightIn,combinedOverlayIn,matrixStack,buffer);
        });
        matrixStack.pop();
    }
}