package ga.beycraft.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import ga.beycraft.BeyRegistry;
import ga.beycraft.blocks.RobotBlock;
import net.minecraft.block.HorizontalBlock;
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
		matrixStack.pushPose();

		matrixStack.translate(0,-0.5,0);
		matrixStack.scale(2,2,2);

		switch (tileEntity.getBlockState().getValue(HorizontalBlock.FACING).get2DDataValue()){
			case 0:
				matrixStack.mulPose(new Quaternion(0,tileEntity.getBlockState().getValue(RobotBlock.FACING).getOpposite().toYRot(),0,true));
				break;
			case 1:
				matrixStack.mulPose(new Quaternion(0,90,0,true));
				matrixStack.translate(0,0,0.5);
				break;
			case 2:
				matrixStack.translate(0.5,0,0.5);
				break;
			case 3:
				matrixStack.mulPose(new Quaternion(0,-90,0,true));
				matrixStack.translate(0.5,0,0);
				break;
		}

		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(BeyRegistry.ROBOT.asItem()),
				ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
		matrixStack.popPose();
		matrixStack.pushPose();
		int i = (int) tileEntity.getBlockState().getValue(RobotBlock.FACING).toYRot();
		matrixStack.mulPose(new Quaternion(0,tileEntity.getBlockState().getValue(RobotBlock.FACING).toYRot(),0,true));


		switch (tileEntity.getBlockState().getValue(RobotBlock.FACING).get2DDataValue()){
			case 0:
				matrixStack.mulPose(new Quaternion(0,180,0,true));
				matrixStack.translate(0,0,-1.1);
				break;
			case 1:
				matrixStack.translate(0,0,-0.1);
				break;
			case 2:
				matrixStack.mulPose(new Quaternion(0,180,0,true));
				matrixStack.translate(1,0,-0.1);
				break;
			case 3:
				matrixStack.translate(1,0,-1.1);
				break;
		}
		matrixStack.scale(0.5f, 0.5f, 0.5f);
		matrixStack.translate(-1.2, 0.4, 0.1);

		matrixStack.mulPose(new Quaternion(90, 0, 90, true));
		tileEntity.getInventory().ifPresent(h -> {
			matrixStack.scale(0.5f, 0.5f, 0.5f);
			matrixStack.translate(0.02, -0.09, 0.3);
			matrixStack.mulPose(new Quaternion(-90, 0, 0, true));
			Minecraft.getInstance().getItemRenderer().renderStatic(h.getStackInSlot(0),
					ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
			matrixStack.mulPose(new Quaternion(90, 0, 0, true));
			matrixStack.translate(-0.02, 0.09, -0.3);
			matrixStack.scale(2f, 2f, 2f);
		});

		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(BeyRegistry.DUALLAUNCHER),
				ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
		matrixStack.translate(0.1, -0.44, -0.04);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(BeyRegistry.LAUNCHERHANDLE),
				ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
		matrixStack.translate(0, 0.03, 0.01);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(BeyRegistry.BEYLOGGERPLUS),
				ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);

		matrixStack.popPose();
	}
}
