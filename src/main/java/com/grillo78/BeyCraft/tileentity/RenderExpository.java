package com.grillo78.BeyCraft.tileentity;

import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDriver;
import com.grillo78.BeyCraft.items.ItemBeyLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderExpository extends TileEntitySpecialRenderer<ExpositoryTileEntity> {
	private int angle;
	private final ExpositorModel model;

	public RenderExpository() {
		this.model = new ExpositorModel();
		this.angle = 0;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void render(ExpositoryTileEntity te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate(x + 0.5, y - 1, z + 0.5);
		Minecraft.getMinecraft().renderEngine
				.bindTexture(new ResourceLocation("minecraft:textures/blocks/concrete_white.png"));
		model.render(0.0625F);
		GlStateManager.translate(0, 1.7, 0);
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.rotate(angle, 0, 0, 1);
		if (te.getStackInSlot(2).getCount() != 0) {
			GlStateManager.translate(0, 0, ((ItemBeyDriver) te.getStackInSlot(2).getItem()).height);
			Minecraft.getMinecraft().getRenderItem().renderItem(te.getStackInSlot(2),
					ItemCameraTransforms.TransformType.FIXED);
		}
		if (te.getStackInSlot(1).getCount() != 0) {
			GlStateManager.translate(0, 0, ((ItemBeyDisk) te.getStackInSlot(1).getItem()).height + 0.01);
			Minecraft.getMinecraft().getRenderItem().renderItem(te.getStackInSlot(1),
					ItemCameraTransforms.TransformType.FIXED);
		}
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		if (te.getStackInSlot(0).getCount() != 0) {
			GlStateManager.translate(0, 0, ((ItemBeyLayer) te.getStackInSlot(0).getItem()).height);
			Minecraft.getMinecraft().getRenderItem().renderItem(te.getStackInSlot(0),
					ItemCameraTransforms.TransformType.FIXED);
		}
		angle++;
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
}
