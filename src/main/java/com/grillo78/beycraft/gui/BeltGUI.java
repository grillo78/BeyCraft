package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.BeltContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author a19guillermong
 *
 */
public class BeltGUI extends ContainerScreen<BeltContainer>{

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public BeltGUI(BeltContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void func_230450_a_(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/2_slots.png"));
		int relX = (this.field_230708_k_ - this.xSize) / 2;
        int relY = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);

	}
}
