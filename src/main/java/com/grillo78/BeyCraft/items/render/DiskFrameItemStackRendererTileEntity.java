package com.grillo78.BeyCraft.items.render;

import com.grillo78.BeyCraft.items.ItemBeyDiskFrame;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class DiskFrameItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	@Override
	public void func_228364_a_(ItemStack stack, MatrixStack matrix, IRenderTypeBuffer buffer, int p_228364_4_,
			int p_228364_5_) {
		Minecraft.getInstance().getItemRenderer().func_229110_a_(new ItemStack(((ItemBeyDiskFrame) stack.getItem())),
				TransformType.FIXED, 0, OverlayTexture.field_229196_a_, matrix, buffer);
		super.func_228364_a_(stack, matrix, buffer, p_228364_4_, p_228364_5_);
	}
}
