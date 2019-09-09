package com.grillo78.BeyCraft.entity.render;

import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.entity.model.ModelVictoryValtryek;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVictoryValtryek extends RenderLiving<EntityBey>{

	protected ResourceLocation victoryValtryekTexture = new ResourceLocation("minecraft", "textures/entity/creeper/creeper.png");
	public static final Factory FACTORY = new Factory();
	public static final ModelVictoryValtryek model = new ModelVictoryValtryek();
	
	public RenderVictoryValtryek(RenderManager rendermanagerIn, ModelVictoryValtryek mainModel) {
		super(rendermanagerIn, new ModelCreeper(),0.5F);
//		setEntityTexture();
	}
	
	protected void setEntityTexture(){
		victoryValtryekTexture = new ResourceLocation(Reference.MODID,"textures/entity/beys/Disk.png");
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityBey entity) {
		return victoryValtryekTexture;
	}

	public static class Factory implements IRenderFactory<EntityBey>{

		@Override
		public Render<? super EntityBey> createRenderFor(RenderManager manager) {
			return new RenderVictoryValtryek(manager, model);
		}
		
	}
}
