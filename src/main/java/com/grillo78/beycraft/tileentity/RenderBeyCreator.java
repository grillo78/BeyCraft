package com.grillo78.beycraft.tileentity;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.blocks.BeyCreatorBlock;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class RenderBeyCreator extends TileEntityRenderer<BeyCreatorTileEntity> {


    public RenderBeyCreator(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(BeyCreatorTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
        matrixStack.push();

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.scale(2,2,2);
        matrixStack.rotate(new Quaternion(0,180,0,true));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-tileEntity.getBlockState().get(BeyCreatorBlock.FACING).getHorizontalAngle()));
        Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(BeyRegistry.BEYCREATORBLOCK.asItem()), ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);

        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.translate(0, -0.5, 0);
        matrixStack.translate(0, 0.14, 0);
        matrixStack.scale(0.45f, 0.45f, 0.45f);
        matrixStack.rotate(new Quaternion(90, 0, 0, true));
        tileEntity.getInventory().ifPresent(h -> {
            if(h.getStackInSlot(0).getItem() instanceof ItemBeyDriver){
                matrixStack.rotate(new Quaternion(180,0,0, true));
                matrixStack.translate(0,0,0.14);
            }
            Minecraft.getInstance().getItemRenderer().renderItem(h.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
        });
        matrixStack.pop();
    }
}
