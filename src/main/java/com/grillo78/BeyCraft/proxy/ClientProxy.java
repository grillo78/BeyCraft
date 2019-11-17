package com.grillo78.BeyCraft.proxy;

import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.entity.RenderBey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{

	@Override
	public void onPreInit() {
		initModels();
		super.onPreInit();
	}
	
	@Override
	public void registerRenders() {
		super.registerRenders();
		RenderingRegistry.registerEntityRenderingHandler(EntityBey.class, new IRenderFactory<EntityBey>() {

			@Override
			public Render<? super EntityBey> createRenderFor(RenderManager manager) {
				return new RenderBey(manager,Minecraft.getMinecraft().getRenderItem());
			}
		});
	}
	
	public static void initModels() {
        OBJLoader.INSTANCE.addDomain(Reference.MODID);
	}
}
