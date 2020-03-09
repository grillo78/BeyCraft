package com.grillo78.BeyCraft.items.render;

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
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Random;

public class BeyItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	@Override
	public void render(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.push();
		IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft","layers/"+stack.getItem().getTranslationKey().replace("item.beycraft.","")));
		matrixStack.translate(0.5F, 0.45F, 0.45F);
		matrixStack.scale(0.5F,0.5F,0.5F);
		IVertexBuilder bb = buffer.getBuffer(RenderType.getEntityCutout(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
		for(BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE))
		{
			bb.addVertexData(matrixStack.getLast(), quad, 1,1,1,1, 1, combinedOverlayIn, true);
		}
		matrixStack.pop();
		super.render(stack, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
		matrixStack.push();
		matrixStack.translate(0.5F, 0.45F, 0.45F);
		matrixStack.rotate(new Quaternion(new Vector3f(0,1,0),-15,true));
		matrixStack.translate(0F,-0.04F,0.25F);
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			Minecraft.getInstance().getItemRenderer().renderItem(h.getStackInSlot(0),
					TransformType.FIRST_PERSON_LEFT_HAND, 0, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
			//matrixStack.translate(0.29f,-0.08f,-0.3f);
			matrixStack.rotate(new Quaternion(new Vector3f(0,1,0),90,true)) ;
			matrixStack.translate(0.25,-0.07,0.25);
			Minecraft.getInstance().getItemRenderer().renderItem(h.getStackInSlot(1),
					TransformType.FIRST_PERSON_LEFT_HAND, 0, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
		});
		matrixStack.pop();
	}


}
