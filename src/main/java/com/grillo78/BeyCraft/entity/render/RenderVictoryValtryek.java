package com.grillo78.BeyCraft.entity.render;

import com.grillo78.BeyCraft.entity.EntityBey;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVictoryValtryek extends RenderLiving{

	protected ResourceLocation victoryValtryekTexture = new ResourceLocation("minecraft", "textures/entity/creeper/creeper.png");
	public static final Factory FACTORY = new Factory();
	
	public RenderVictoryValtryek(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCreeper(),0.5F);
	}
	
	protected void preRenderCallback(EntityCreeper entitylivingbaseIn, float partialTickTime)
    {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return victoryValtryekTexture;
	}

	public static class Factory implements IRenderFactory<EntityBey>{

		@Override
		public Render<? super EntityBey> createRenderFor(RenderManager manager) {
			return new RenderVictoryValtryek(manager);
		}
		
	}
}
