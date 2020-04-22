package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.BeyContainer;
import com.grillo78.beycraft.inventory.BeyGTContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author grillo78
 *
 */
public class BeyGTGUI extends ContainerScreen<BeyGTContainer>{

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public BeyGTGUI(BeyGTContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground();
		super.render(p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredToolTip(p_render_1_, p_render_2_);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/2_slots.png"));
		int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
	}
}
