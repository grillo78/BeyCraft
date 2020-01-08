package com.grillo78.BeyCraft.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class BeyRender extends EntityRenderer<EntityBey>{

	protected BeyRender(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityBey entity) {
		return null;
	}

	@Override
	public void func_225623_a_(EntityBey entity, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack,
			IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
		if(this.renderManager.pointedEntity == entity && !Minecraft.getInstance().player.isSpectator()) {
			matrixStack.func_227860_a_();
			matrixStack.func_227861_a_(0, 0.5F, 0);
			func_225629_a_(entity, entity.getLayer().getItem().getName().getFormattedText(), matrixStack, p_225623_5_, p_225623_6_);
			matrixStack.func_227861_a_(0, -0.25F, 0);
			func_225629_a_(entity, entity.getDisk().getItem().getName().getFormattedText(), matrixStack, p_225623_5_, p_225623_6_);
			matrixStack.func_227861_a_(0, -0.25F, 0);
			func_225629_a_(entity, entity.getDriver().getItem().getName().getFormattedText(), matrixStack, p_225623_5_, p_225623_6_);
			matrixStack.func_227865_b_();
		}
		matrixStack.func_227860_a_();
        matrixStack.func_227862_a_(0.5F, 0.5F, 0.5F);
		matrixStack.func_227863_a_(new Quaternion(90, 0, 0, true));
		matrixStack.func_227861_a_(0, 0, -0.3);
		matrixStack.func_227863_a_(new Quaternion(0, 0, entity.getRotationSpeed()*900, true));
		Minecraft.getInstance().getItemRenderer().func_229110_a_(entity.getLayer(), TransformType.FIXED, 0, OverlayTexture.field_229196_a_, matrixStack, p_225623_5_);
		matrixStack.func_227863_a_(new Quaternion(0, 0, entity.getRotationDirection()*37, true));
		matrixStack.func_227861_a_(0, 0, 0.08);
		Minecraft.getInstance().getItemRenderer().func_229110_a_(entity.getDisk(), TransformType.FIXED, 0, OverlayTexture.field_229196_a_, matrixStack, p_225623_5_);
		matrixStack.func_227861_a_(0, 0, 0.15);
		Minecraft.getInstance().getItemRenderer().func_229110_a_(entity.getDriver(), TransformType.FIXED, 0, OverlayTexture.field_229196_a_, matrixStack, p_225623_5_);
		matrixStack.func_227865_b_();
	}
	
}
