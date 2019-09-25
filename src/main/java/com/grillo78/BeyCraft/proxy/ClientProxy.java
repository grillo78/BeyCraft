package com.grillo78.BeyCraft.proxy;

import com.grillo78.BeyCraft.Reference;

import net.minecraftforge.client.model.obj.OBJLoader;
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
	}
	
	public static void initModels() {
        OBJLoader.INSTANCE.addDomain(Reference.MODID);
	}
}
