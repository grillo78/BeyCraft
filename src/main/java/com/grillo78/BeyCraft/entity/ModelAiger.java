package com.grillo78.BeyCraft.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelAiger extends ModelBiped {

	private final EntityEquipmentSlot slot;

	// Chest
	private ModelRenderer body;
	private ModelRenderer bodyLayer;
	private ModelRenderer armLeft;
	private ModelRenderer armHandLeft;
	private ModelRenderer armRight;
	private ModelRenderer armRightLayer;

	// Leggings
	private ModelRenderer legLeft;
	private ModelRenderer legRight;

	// Boots
	private ModelRenderer footRight;
	private ModelRenderer footLeft;

	public ModelAiger(float scale, EntityEquipmentSlot slot) {
		super(scale, 0, 64, 64);

		this.slot = slot;
		switch (slot) {
		case CHEST:
			this.body = new ModelRenderer(this, 0, 0);
			this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
			this.body.cubeList.add(new ModelBox(body, 0, 18, -4.5F, 0.0F, -2.0F, 9, 12, 4, 0.05F, false));
			this.bodyLayer = new ModelRenderer(this, 28, 0);
			this.bodyLayer.setRotationPoint(0.0F, 0.0F, 0.0F);
			this.bodyLayer.cubeList.add(new ModelBox(bodyLayer, 0, 0, -5.0F, -0.5F, -2.5F, 10, 13, 5, 0.0F, false));
			this.armLeft = new ModelRenderer(this, 0, 18);
			this.armLeft.setRotationPoint(5.0F, 2.0F, 0.0F);
			armLeft.cubeList.add(new ModelBox(armLeft, 40, 40, -1.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F, false));
			this.armRight = new ModelRenderer(this, 0, 18);
			this.armRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
			armRight.cubeList.add(new ModelBox(armRight, 0, 34, -3.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F, false));
			this.armHandLeft = new ModelRenderer(this, 20, 18);
			this.armHandLeft.setRotationPoint(0.0F, 8.0F, 0.0F);
			armHandLeft.cubeList.add(new ModelBox(armHandLeft, 20, 38, -1.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F, false));
			this.armRightLayer = new ModelRenderer(this, 20, 18);
			this.armRightLayer.setRotationPoint(0.0F, 8.0F, 0.0F);
			armRightLayer.cubeList.add(new ModelBox(armRightLayer, 30, 0, -3.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F, false));

			this.bipedBody = body;
			this.body.addChild(this.bodyLayer);

			this.bipedLeftArm = armLeft;
			this.armLeft.addChild(this.armHandLeft);

			this.bipedRightArm = armRight;
			this.armRight.addChild(this.armRightLayer);
			break;

		case LEGS:
			this.legLeft = new ModelRenderer(this, 0, 28);
			this.legLeft.setRotationPoint(1.9F, 12.0F, 0.0F);
			legLeft.cubeList.add(new ModelBox(legLeft, 26, 13, -2.8F, 0.0F, -2.5F, 5, 7, 5, 0.0F, false));
			this.legRight = new ModelRenderer(this, 0, 28);
			this.legRight.mirror = true;
			this.legRight.setRotationPoint(-1.9F, 12.0F, 0.0F);
			legRight.cubeList.add(new ModelBox(legRight, 26, 26, -2.2F, 0.0F, -2.5F, 5, 7, 5, 0.0F, false));

			this.bipedLeftLeg = legLeft;

			this.bipedRightLeg = legRight;
			break;

		case FEET:
			this.footLeft = new ModelRenderer(this, 20, 28);
			this.footLeft.setRotationPoint(1.9F, 12.0F, 0.0F);
			footLeft.cubeList.add(new ModelBox(footLeft, 41, 20, -2.5F, 8.5F, -2.5F, 5, 4, 5, 0.0F, false));
			this.footRight = new ModelRenderer(this, 20, 28);
			this.footRight.mirror = true;
			this.footRight.setRotationPoint(-1.9F, 12.0F, 0.0F);
			footRight.cubeList.add(new ModelBox(footRight, 0, 44, -2.5F, 8.5F, -2.5F, 5, 4, 5, 0.0F, false));

			this.bipedLeftLeg = footLeft;

			this.bipedRightLeg = footRight;
			break;

		default:
			break;
		}
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadyaw,
			float headPitch, float scaleFactor) {
		this.setVisible(false);

		switch (this.slot) {
		case CHEST:
			bipedBody.showModel = true;
			bipedLeftArm.showModel = true;
			bipedRightArm.showModel = true;
			break;

		case LEGS:
			bipedLeftLeg.showModel = true;
			bipedRightLeg.showModel = true;
			break;

		case FEET:
			bipedLeftLeg.showModel = true;
			bipedRightLeg.showModel = true;
			break;

		default:
			break;
		}
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadyaw, headPitch, scaleFactor);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
