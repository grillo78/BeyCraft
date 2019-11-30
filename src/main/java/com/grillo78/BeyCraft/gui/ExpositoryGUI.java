package com.grillo78.BeyCraft.gui;

import com.grillo78.BeyCraft.inventory.ExpositoryGuiContainer;
import com.grillo78.BeyCraft.tileentity.ExpositoryTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExpositoryGUI extends GuiContainer {

	private InventoryPlayer playerInventory;
	private ExpositoryTileEntity tileentity;

	public ExpositoryGUI(InventoryPlayer inventory, ExpositoryTileEntity tileentity) {
		super(new ExpositoryGuiContainer(inventory, tileentity));
		playerInventory = Minecraft.getMinecraft().player.inventory;
		this.tileentity = tileentity;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        this.mc.getTextureManager().bindTexture(PRINTER_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

}
