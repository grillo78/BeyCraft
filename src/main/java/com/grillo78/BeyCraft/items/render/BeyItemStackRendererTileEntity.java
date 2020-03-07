package com.grillo78.BeyCraft.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

public class BeyItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	@Override
	public void render(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.push();
		matrixStack.translate(0.5F, 0.5F, 0.5F);
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			Minecraft.getInstance().getItemRenderer().renderItem(h.getStackInSlot(0),
					TransformType.FIRST_PERSON_LEFT_HAND, 0, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
			Minecraft.getInstance().getItemRenderer().renderItem(h.getStackInSlot(1),
					TransformType.FIRST_PERSON_LEFT_HAND, 0, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
		});
		matrixStack.pop();
		super.render(stack, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
	}


}
