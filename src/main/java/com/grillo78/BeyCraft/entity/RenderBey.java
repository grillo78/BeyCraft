package com.grillo78.BeyCraft.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderBey extends RenderLiving<EntityBey> {

	private final RenderItem itemRenderer;

	public RenderBey(RenderManager rendermanagerIn, RenderItem itemRendererIn) {
		super(rendermanagerIn, new ModelBase() {
		}, 0.1F);
		this.itemRenderer = itemRendererIn;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBey entity) {
		return null;
	}

	@Override
	public void doRender(EntityBey entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate(x, y + 0.136, z);
		GlStateManager.scale(0.5, 0.5, 0.5);
		GlStateManager.translate(0, -0.02, 0);

		if (entity.getRadius() != 0
				&& entity.getRotationSpeed() < -2) {
			GlStateManager.rotate(30, (float) entity.getLook(partialTicks).x * entity.getRotationDirection(), 0,
					(float) entity.getLook(partialTicks).z * entity.getRotationDirection());
		} else {
			if (entity.getRadius() == 0 && entity.getRotationSpeed() < -2) {
				GlStateManager.translate(0, 0.04, 0);
			}
		}
		GlStateManager.rotate(entity.angle, 0, 1, 0);
		if (entity.getRotationSpeed() > -2) {
			GlStateManager.translate(0, (1 - (-2 - entity.getRotationSpeed()) / -2) * 0.04, 0);
			GlStateManager.rotate(40 * ((-2 - entity.getRotationSpeed()) / (-2)), (float) entity.getLook(partialTicks).z, 0,
					(float) -entity.getLook(partialTicks).x);
		}
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.translate(0, 0, 0.08);
		GlStateManager.translate(0, 0, entity.driver.height + 0.01);
		itemRenderer.renderItem(new ItemStack(entity.driver), ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.translate(0, 0, entity.disk.height);
		itemRenderer.renderItem(new ItemStack(entity.disk), ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.translate(0, 0, entity.layer.height);
		GlStateManager.rotate(90 * entity.getHealth() / 1024 - 40, 0, 0, entity.getRotationDirection());
		itemRenderer.renderItem(new ItemStack(entity.layer), ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.disableOutlineMode();
		GlStateManager.disableColorMaterial();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		if (this.renderManager.pointedEntity == entity && !Minecraft.getMinecraft().player.isSpectator()) {
			this.renderEntityName(entity, x, y + 0.9, z, new ItemStack(entity.layer).getDisplayName(), 10);
			this.renderEntityName(entity, x, y + 0.6, z, new ItemStack(entity.disk).getDisplayName(), 10);
			this.renderEntityName(entity, x, y + 0.3, z, new ItemStack(entity.driver).getDisplayName(), 10);
			this.renderEntityName(entity, x, y + 1.2, z, "Health: " + entity.getHealth(), 10);
			if (entity.isStoped()) {
				this.renderEntityName(entity, x, y, z, "Speed: " + entity.getRotationSpeed(), 10);
			} else {
				this.renderEntityName(entity, x, y, z, "Speed: " + (-entity.getRotationSpeed()), 10);
			}
		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}