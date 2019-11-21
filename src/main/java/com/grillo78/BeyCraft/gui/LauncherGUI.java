package com.grillo78.BeyCraft.gui;

import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.inventory.LauncherGuiContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LauncherGUI extends GuiContainer{
	private static final ResourceLocation LAUNCHER_GUI_TEXTURE = new ResourceLocation(Reference.MODID,"textures/gui/container/launcher.png");
	public LauncherGUI(LauncherGuiContainer inventorySlotsIn, InventoryPlayer inventory) {
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
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString("Layer", 30, 15, 4210752);
		this.fontRenderer.drawString("Disk", 30, 35, 4210752);
		this.fontRenderer.drawString("Driver", 30, 55, 4210752);
		this.fontRenderer.drawString("Handle", 82, 15, 4210752);
		this.fontRenderer.drawString("BeyLogger", 82, 35, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(LAUNCHER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
