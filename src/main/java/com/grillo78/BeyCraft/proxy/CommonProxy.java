package com.grillo78.BeyCraft.proxy;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.gui.BeyGUI;
import com.grillo78.BeyCraft.inventory.BeyGuiContainer;
import com.grillo78.BeyCraft.items.ItemBeyLayer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy implements IGuiHandler{
	public void registerRenders() {
	}

	public void onPreInit() {
		NetworkRegistry.INSTANCE.registerGuiHandler(BeyCraft.instance, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID==0) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemBeyLayer) {
				return new BeyGuiContainer(player.inventory, player.getHeldItemMainhand());
			}else {
				return new BeyGuiContainer(player.inventory, player.getHeldItemOffhand());
			}
			
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID==0) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemBeyLayer) {
				return new BeyGUI(new BeyGuiContainer(player.inventory, player.getHeldItemMainhand()), player.inventory);
			}else {
				return new BeyGUI(new BeyGuiContainer(player.inventory, player.getHeldItemOffhand()), player.inventory);
			}
		}
		return null;
	}
}
