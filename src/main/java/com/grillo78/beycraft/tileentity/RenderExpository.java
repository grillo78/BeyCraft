package com.grillo78.beycraft.tileentity;

import com.grillo78.beycraft.BeyRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class RenderExpository extends TileEntityRenderer<ExpositoryTileEntity> {

    private float beyRotation = 0;


    public RenderExpository(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(ExpositoryTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
        matrixStack.push();

        matrixStack.scale(2, 2, 2);
        matrixStack.translate(0.25, 0.25, 0.25);
        Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(BeyRegistry.EXPOSITORY.asItem()), ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
        matrixStack.translate(0, 0.15, 0);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.rotate(new Quaternion(90, 0, 0, true));
        if(!Minecraft.getInstance().isGamePaused()){
            if (beyRotation < 360) {
                beyRotation += 5;
            } else {
                beyRotation = 0;
            }
            matrixStack.rotate(new Quaternion(0, 0, beyRotation, true));
        }
        tileEntity.getInventory().ifPresent(h -> {
            Minecraft.getInstance().getItemRenderer().renderItem(h.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
        });
        matrixStack.pop();
    }
}
