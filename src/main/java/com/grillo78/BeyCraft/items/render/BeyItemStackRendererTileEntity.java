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
	public void func_228364_a_(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_228364_4_,
			int p_228364_5_) {
		matrixStack.func_227860_a_();
		matrixStack.func_227862_a_(0.5F, 0.5F, 0.5F);
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			Minecraft.getInstance().getItemRenderer().func_229110_a_(h.getStackInSlot(0),
					TransformType.FIRST_PERSON_LEFT_HAND, 0, OverlayTexture.field_229196_a_, matrixStack, buffer);
			Minecraft.getInstance().getItemRenderer().func_229110_a_(h.getStackInSlot(1),
					TransformType.FIRST_PERSON_LEFT_HAND, 0, OverlayTexture.field_229196_a_, matrixStack, buffer);
		});
		matrixStack.func_227865_b_();
		super.func_228364_a_(stack, matrixStack, buffer, p_228364_4_, p_228364_5_);
	}
}
