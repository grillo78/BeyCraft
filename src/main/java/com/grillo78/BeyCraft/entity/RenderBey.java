package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDriver;
import com.grillo78.BeyCraft.items.ItemBeyLayer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBey extends RenderLiving<EntityBey> {

	private final RenderItem itemRenderer;

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
		if (!entity.getDroppedItem()) {
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			GlStateManager.translate(x, y + 0.15, z);
			GlStateManager.scale(0.5, 0.5, 0.5);
			GlStateManager.translate(0, -0.02, 0);

			if (entity.radius != 0F && entity.rotationSpeed < -1) {
				GlStateManager.rotate(30, (float) entity.getLook(partialTicks).x * entity.getRotationDirection(), 0,
						(float) entity.getLook(partialTicks).z * entity.getRotationDirection());
			} else {
				if(entity.radius==0 && entity.rotationSpeed < -1) {
					GlStateManager.translate(0, 0.04, 0);
				}
			}
			GlStateManager.rotate(entity.angle / 2, 0, 1, 0);
			if (entity.rotationSpeed <= 0 && entity.rotationSpeed > -1) {
				GlStateManager.rotate(30, (float) entity.getLook(partialTicks).z, 0,
						(float) -entity.getLook(partialTicks).x);
			}
			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.translate(0, 0, 0.08);
			GlStateManager.translate(0, 0, ((ItemBeyDriver) entity.driver.getItem()).height + 0.01);
			itemRenderer.renderItem(entity.driver, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.translate(0, 0, ((ItemBeyDisk) entity.disk.getItem()).height);
			itemRenderer.renderItem(entity.disk, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.translate(0, 0, ((ItemBeyLayer) entity.layer.getItem()).height);
			GlStateManager.rotate(entity.getHealth() * entity.getRotationDirection() + 40, 0, 0, 1);
			itemRenderer.renderItem(entity.layer, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
			if (this.renderManager.pointedEntity == entity) {
				this.renderEntityName(entity, x, y + 0.6, z, entity.layer.getDisplayName(), 10);
				this.renderEntityName(entity, x, y + 0.3, z, entity.disk.getDisplayName(), 10);
				this.renderEntityName(entity, x, y, z, entity.driver.getDisplayName(), 10);
			}
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}
}