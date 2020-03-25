package com.grillo78.beycraft.items.render;

import com.grillo78.beycraft.items.ItemBeyDiscFrame;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class DiskFrameItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	@Override
	public void render(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn,
			int combinedOverlayIn) {
		matrixStack.push();
		matrixStack.translate(0.5F, 0.5F, 0.5F);
		Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(((ItemBeyDiscFrame) stack.getItem())),
				TransformType.FIRST_PERSON_LEFT_HAND, 0, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
		matrixStack.pop();
		super.render(stack, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
	}
}
