package com.grillo78.beycraft.items.render;

import com.grillo78.beycraft.items.ItemBeyDiscFrame;
import com.grillo78.beycraft.util.CachedStacks;
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

import java.util.HashMap;
import java.util.Random;

public class DiscFrameItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	private HashMap<CompoundNBT, ItemStack> stacks;
	private Random random = new Random();

	public DiscFrameItemStackRendererTileEntity() {
		stacks = CachedStacks.INSTANCE.getStacks();
	}

	@Override
	public void renderByItem(ItemStack stack, TransformType transformType, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.pushPose();

		IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft",
				"discsframe/" + stack.getItem().getRegistryName().getPath()));
		IVertexBuilder vertexBuilder = buffer
				.getBuffer(RenderType.entityTranslucent(AtlasTexture.LOCATION_BLOCKS));
		MatrixStack.Entry entry = matrixStack.last();
		for (BakedQuad quad : model.getQuads(null, null, random, EmptyModelData.INSTANCE)) {
			vertexBuilder.addVertexData(entry, quad, 1, 1, 1, 1, combinedLightIn,combinedOverlayIn, true);
		}
		if (stack.hasTag()) {
			matrixStack.scale(2F, 2F, 2F);
			matrixStack.mulPose(new Quaternion(
					new Vector3f(0, ((ItemBeyDiscFrame) stack.getItem()).getFrameRotation(), 0), -15, true));
			matrixStack.translate(0F, 0.01F, 0.25F);
			if (!stacks.containsKey(stack.getTag().get("frame"))) {
				stacks.put((CompoundNBT) stack.getTag().get("frame"),
						ItemStack.of((CompoundNBT) stack.getTag().get("frame")));
			}
			Minecraft.getInstance().getItemRenderer().renderStatic(stacks.get(stack.getTag().get("frame")),
					TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);

		}

		matrixStack.popPose();
		super.renderByItem(stack, transformType, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
	}
}
