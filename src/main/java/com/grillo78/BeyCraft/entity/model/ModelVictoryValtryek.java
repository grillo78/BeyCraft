package com.grillo78.BeyCraft.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVictoryValtryek extends ModelBase {
	private ModelRenderer Bey;
	private ModelRenderer Layer;
	private ModelRenderer Disk;
	private ModelRenderer Driver;

	public ModelVictoryValtryek() {
		textureWidth = 16;
		textureHeight = 16;

		Bey = new ModelRenderer(this);
		Bey.setRotationPoint(0.0F, 24.0F, 0.0F);

		Layer = new ModelRenderer(this);
		Layer.setRotationPoint(0.0F, 0.0F, 0.0F);
		Layer.cubeList.add(new ModelBox(Layer, 0, 0, -3.0F, -3.0F, -2.0F, 5, 1, 5, 0.0F, false));
		Bey.addChild(Layer);

		Disk = new ModelRenderer(this);
		Disk.setRotationPoint(0.0F, 0.0F, 0.0F);
		Disk.cubeList.add(new ModelBox(Disk, 0, 0, -2.0F, -2.0F, -1.0F, 3, 1, 3, 0.0F, false));
		Bey.addChild(Disk);

		Driver = new ModelRenderer(this);
		Driver.setRotationPoint(0.0F, 0.0F, 0.0F);
		Driver.cubeList.add(new ModelBox(Driver, 0, 0, -1.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
		Bey.addChild(Driver);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		Bey.render(scale);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}