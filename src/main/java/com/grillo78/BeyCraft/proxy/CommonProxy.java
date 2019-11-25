package com.grillo78.BeyCraft.proxy;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.capabilities.BladerLevelStorage;
import com.grillo78.BeyCraft.capabilities.Factory;
import com.grillo78.BeyCraft.capabilities.IBladerLevel;
import com.grillo78.BeyCraft.gui.LauncherGUI;
import com.grillo78.BeyCraft.inventory.LauncherGuiContainer;
import com.grillo78.BeyCraft.items.ItemLauncher;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy implements IGuiHandler {
	public void registerRenders() {

	}

	public void onPreInit() {
		NetworkRegistry.INSTANCE.registerGuiHandler(BeyCraft.instance, this);
		CapabilityManager.INSTANCE.register(IBladerLevel.class, new BladerLevelStorage(), new Factory());
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemLauncher) {
				return new LauncherGuiContainer(player.inventory, player.getHeldItemMainhand(),
						((ItemLauncher) player.getHeldItemMainhand().getItem()).getRotation());
			} else {
				return new LauncherGuiContainer(player.inventory, player.getHeldItemOffhand(),
						((ItemLauncher) player.getHeldItemOffhand().getItem()).getRotation());
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemLauncher) {
				return new LauncherGUI(
						new LauncherGuiContainer(player.inventory, player.getHeldItemMainhand(),
								((ItemLauncher) player.getHeldItemMainhand().getItem()).getRotation()),
						player.inventory);
			} else {
				return new LauncherGUI(
						new LauncherGuiContainer(player.inventory, player.getHeldItemOffhand(),
								((ItemLauncher) player.getHeldItemOffhand().getItem()).getRotation()),
						player.inventory);
			}
		}
		return null;
	}
}
