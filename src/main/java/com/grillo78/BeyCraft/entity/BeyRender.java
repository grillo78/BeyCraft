package com.grillo78.BeyCraft.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class BeyRender extends EntityRenderer<EntityBey> {

	protected BeyRender(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityBey entity) {
		return null;
	}

	@Override
	public void render(EntityBey entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer bufferIn, int packedLightIn) {
		if (this.renderManager.pointedEntity == entity && !Minecraft.getInstance().player.isSpectator()
				&& Minecraft.isGuiEnabled()) {
			matrixStack.push();
			matrixStack.translate(0, 0.5F, 0);
			renderName(entity, entity.getLayer().getItem().getName().getFormattedText(), matrixStack, bufferIn,
					packedLightIn);
			matrixStack.translate(0, -0.25F, 0);
			renderName(entity, entity.getDisk().getItem().getName().getFormattedText(), matrixStack, bufferIn,
					packedLightIn);
			matrixStack.translate(0, -0.25F, 0);
			renderName(entity, entity.getDriver().getItem().getName().getFormattedText(), matrixStack, bufferIn,
					packedLightIn);
			matrixStack.translate(0, -0.25F, 0);
			renderName(entity, "Speed:" + entity.getRotationSpeed(), matrixStack, bufferIn, packedLightIn);
			matrixStack.pop();
		}
		matrixStack.push();
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		matrixStack.rotate(new Quaternion(90, 0, 0, true));
		matrixStack.translate(0, 0, -0.25);
		if (entity.getRadius() != 0 && entity.getRotationSpeed() > 2) {
			matrixStack.rotate(
					new Quaternion(new Vector3f((float) entity.getLookVec().x * entity.getRotationDirection(), 0,
							(float) entity.getLookVec().z * entity.getRotationDirection()), -30, true));
		}
		matrixStack.rotate(new Quaternion(0, 0, entity.angle * 2, true));
		if (entity.getRotationSpeed() < 2) {
			matrixStack.rotate(
					new Quaternion(new Vector3f((float) entity.getLookVec().x, (float) entity.getLookVec().z, 0),
							40 * ((2 - entity.getRotationSpeed()) / (2)), true));
		}
		Minecraft.getInstance().getItemRenderer().renderItem(entity.getLayer(), TransformType.FIXED, packedLightIn,
				OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
		matrixStack.rotate(new Quaternion(0, 0,
				entity.getRotationDirection() * -entity.getHealth() + entity.getRotationDirection()+85	, true));
		matrixStack.translate(0, 0, 0.078);
		Minecraft.getInstance().getItemRenderer().renderItem(entity.getDisk(), TransformType.FIXED, packedLightIn,
				OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
		matrixStack.translate(0, 0, 0.15);
		Minecraft.getInstance().getItemRenderer().renderItem(entity.getDriver(), TransformType.FIXED, packedLightIn,
				OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
		matrixStack.pop();
	}

}
