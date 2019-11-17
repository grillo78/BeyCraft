package com.grillo78.BeyCraft.entity;

import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBey extends RenderLiving<EntityBey>{

	public RenderBey(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBoat(), 0.1F);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBey entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
