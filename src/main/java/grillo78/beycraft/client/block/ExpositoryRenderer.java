package grillo78.beycraft.client.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import grillo78.beycraft.common.block_entity.ExpositoryTileEntity;
import grillo78.beycraft.common.item.BeyPartItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class ExpositoryRenderer extends TileEntityRenderer<ExpositoryTileEntity> {
    public ExpositoryRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(ExpositoryTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
        matrixStack.pushPose();
        tileEntity.getInventory().ifPresent(h -> {
            if (h.getStackInSlot(0).getItem() instanceof BeyPartItem) {
                matrixStack.translate(0.5,0.25,0.5);
                matrixStack.scale(1.6F,1.6F,1.6F);
                Minecraft.getInstance().getItemRenderer().renderStatic(h.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND, light, overlay, matrixStack, iRenderTypeBuffer);
            }
        });
        matrixStack.popPose();
    }
}
