package com.grillo78.beycraft.proxy;

public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit() {
        super.onPreInit();
    }

    @Override
    public void registerRenders() {
//		ClientRegistry.bindTileEntityRenderer(BeyRegistry.EXPOSITORY_TILE_ENTITY_TYPE.get(), RenderExpository::new);
    }
}
