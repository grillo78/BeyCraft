package com.grillo78.beycraft.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class BeyEntityRenderFactory implements IRenderFactory<EntityBey> {

	@Override
	public EntityRenderer<EntityBey> createRenderFor(EntityRendererManager manager) {
		return new BeyRender(manager);
	}

}
