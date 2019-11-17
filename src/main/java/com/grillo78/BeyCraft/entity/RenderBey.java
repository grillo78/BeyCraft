package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.BeyRegistry;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderBey extends RenderLiving<EntityBey>{

    private final RenderItem itemRenderer;
//	public ModelBase modelBey = new ModelTest();
	public RenderBey(RenderManager rendermanagerIn, RenderItem itemRendererIn) {
		super(rendermanagerIn, new ModelTest(), 0.1F);
        this.itemRenderer = itemRendererIn;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBey entity) {
		return null;
	}

	@Override
	public void doRender(EntityBey entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y+0.28, z);
		GlStateManager.rotate(90, 1, 0, 0);
		itemRenderer.renderItem(new ItemStack(BeyRegistry.ACHILLESA4), ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.rotate(-40, 0, 0, 1);
		GlStateManager.translate(0, 0, 0.08);
		itemRenderer.renderItem(new ItemStack(BeyRegistry.ELEVENDISK), ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.translate(0, 0, 0.15);
		itemRenderer.renderItem(new ItemStack(BeyRegistry.XTENDDRIVER), ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
