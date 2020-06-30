package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.HandleContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class HandleGUI extends ContainerScreen<HandleContainer>{

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public HandleGUI(HandleContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

//	@Override
//	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
//		this.renderBackground();
//		super.render(p_render_1_, p_render_2_, p_render_3_);
//		this.renderHoveredToolTip(p_render_1_, p_render_2_);
//	}

	@Override
	protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/launcher.png"));
		int relX = (this.field_230708_k_ - this.xSize) / 2;
        int relY = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
	}
}
