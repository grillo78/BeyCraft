package com.grillo78.BeyCraft.proxy;

public class ClientProxy extends CommonProxy{

	@Override
	public void onPreInit() {
		super.onPreInit();
	}
	
	@Override
	public void registerRenders() {
//		RenderingRegistry.registerEntityRenderingHandler(EntityBey.class, new IRenderFactory<EntityBey>() {
//
//			@Override
//			public Render<? super EntityBey> createRenderFor(RenderManager manager) {
//				return new RenderBey(manager,Minecraft.getMinecraft().getRenderItem());
//			}
//		});
//		ClientRegistry.bindTileEntitySpecialRenderer(ExpositoryTileEntity.class, new RenderExpository());
	}
}
