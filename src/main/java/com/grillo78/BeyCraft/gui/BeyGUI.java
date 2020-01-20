package com.grillo78.BeyCraft.gui;

import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.inventory.BeyContainer;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author a19guillermong
 *
 */
public class BeyGUI extends ContainerScreen<BeyContainer>{

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public BeyGUI(BeyContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/launcher.png"));
		int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
	}
}
