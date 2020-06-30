package com.grillo78.beycraft.tileentity;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.blocks.RobotBlock;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class RenderRobot extends TileEntityRenderer<RobotTileEntity> {

	private float beyRotation = 0;

	public RenderRobot(TileEntityRendererDispatcher p_i226006_1_) {
		super(p_i226006_1_);
	}

	@Override
	public void render(RobotTileEntity tileEntity, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
		matrixStack.push();
		matrixStack.rotate(new Quaternion(90,0,0,true));
		matrixStack.rotate(tileEntity.getBlockState().get(RobotBlock.FACING).getRotation());
		switch ((int) tileEntity.getBlockState().get(RobotBlock.FACING).getHorizontalAngle()) {
		case 270:
			matrixStack.translate(-1, -0.5, 0);
			break;
		case 180:
			matrixStack.translate(-1, -0.5, -1);
			break;
		case 90:
			matrixStack.translate(0, -0.5, -1);
			break;
		case 0:
			matrixStack.translate(0, -0.5, 0);
			break;
		}
		matrixStack.rotate(new Quaternion(0, 180, 0, true));
		Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(BeyRegistry.ROBOT.asItem()),
				ItemCameraTransforms.TransformType.NONE, light, overlay, matrixStack, iRenderTypeBuffer);
		matrixStack.pop();
		matrixStack.push();
		matrixStack.rotate(new Quaternion(90,0,0,true));
		switch ((int) tileEntity.getBlockState().get(RobotBlock.FACING).getHorizontalAngle()) {
		case 270:
			matrixStack.translate(1, 0, 0);
			break;
		case 90:
			matrixStack.translate(0, 0, 1);
			break;
		case 0:
			matrixStack.translate(1, 0, 1);
			break;
		}
		int i = (int) tileEntity.getBlockState().get(RobotBlock.FACING).getHorizontalAngle();
		matrixStack.rotate(tileEntity.getBlockState().get(RobotBlock.FACING).getRotation());

		matrixStack.scale(0.5f, 0.5f, 0.5f);
		matrixStack.translate(-1.2, 0.4, 0.1);

		matrixStack.rotate(new Quaternion(90, 0, 90, true));
		tileEntity.getInventory().ifPresent(h -> {
			matrixStack.scale(0.5f, 0.5f, 0.5f);
			matrixStack.translate(0, -0.075, 0.05);
			matrixStack.rotate(new Quaternion(0, 0, 50, true));
			Minecraft.getInstance().getItemRenderer().renderItem(h.getStackInSlot(0),
					ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
			matrixStack.rotate(new Quaternion(0, 0, -50, true));
			matrixStack.translate(0, 0.075, -0.05);
			matrixStack.scale(2f, 2f, 2f);
		});
		Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(BeyRegistry.DUALLAUNCHER),
				ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
		matrixStack.translate(0.1, -0.44, -0.04);
		Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(BeyRegistry.LAUNCHERHANDLE),
				ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
		matrixStack.translate(0, 0.03, 0.01);
		Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(BeyRegistry.BEYLOGGERPLUS),
				ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);

		matrixStack.pop();
	}
}
