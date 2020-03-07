package com.grillo78.BeyCraft.proxy;

import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.tileentity.ExpositoryTileEntity;
import com.grillo78.BeyCraft.tileentity.RenderExpository;
import net.minecraftforge.fml.client.registry.ClientRegistry;

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
