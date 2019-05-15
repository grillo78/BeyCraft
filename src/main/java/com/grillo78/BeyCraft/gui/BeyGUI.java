package com.grillo78.BeyCraft.gui;

import com.grillo78.BeyCraft.inventory.BeyGuiContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BeyGUI extends GuiContainer{
	public BeyGUI(BeyGuiContainer inventorySlotsIn, InventoryPlayer playerInv) {
		super(inventorySlotsIn);
	}

	@Override
	public void initGui() {
		
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
	}
}
