package com.grillo78.BeyCraft.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * ExpositorModel - Undefined
 * Created using Tabula 7.0.0
 */
public class ExpositoryModel extends ModelBase {
    public ModelRenderer shape1;

    public ExpositoryModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(-8.0F, 16.0F, -8.0F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 16, 8, 16, 0.0F);
    }

    public void render(float f5) { 
        this.shape1.render(f5);
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
