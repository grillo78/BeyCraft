package ga.beycraft.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import ga.beycraft.util.CustomRenderType;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RenderBattleInformer extends TileEntityRenderer<BattleInformerTileEntity> {

    public RenderBattleInformer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(BattleInformerTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
        matrixStack.pushPose();

        float angle = tileEntity.getBlockState().getValue(ChestBlock.FACING).toYRot();
        matrixStack.mulPose(new Quaternion(0, -angle, 0, true));
        matrixStack.translate(0, 0, 0.5);
        switch (tileEntity.getBlockState().getValue(ChestBlock.FACING)) {
            case NORTH:
                matrixStack.translate(-1, 0, -1);
                break;
            case WEST:
                matrixStack.translate(0, 0, -1);
                break;
            case EAST:
                matrixStack.translate(-1, 0, 0);
                break;
        }
        AtomicReference<IVertexBuilder> builder = new AtomicReference<>(iRenderTypeBuffer.getBuffer(CustomRenderType.getHologram()));
        AtomicReference<Matrix4f> matrix4f = new AtomicReference<>(matrixStack.last().pose());


        builder.get().vertex(matrix4f.get(), -0.1F, 0.75F, 0).color(72, 153, 173, 0).endVertex();
        builder.get().vertex(matrix4f.get(), 1.1F, 0.75F, 0).color(72, 153, 173, 0).endVertex();

        builder.get().vertex(matrix4f.get(), 0.6F, 0.34F, 0).color(72, 153, 173, 255).endVertex();
        builder.get().vertex(matrix4f.get(), 0.4F, 0.34F, 0).color(72, 153, 173, 255).endVertex();

        builder.get().vertex(matrix4f.get(), 1.1F, 0.75F, 0).color(72, 153, 173, 0).endVertex();
        builder.get().vertex(matrix4f.get(), -0.1F, 0.75F, 0).color(72, 153, 173, 0).endVertex();

        builder.get().vertex(matrix4f.get(), 0.4F, 0.34F, 0).color(72, 153, 173, 255).endVertex();
        builder.get().vertex(matrix4f.get(), 0.6F, 0.34F, 0).color(72, 153, 173, 255).endVertex();

        matrixStack.translate(0, 0.75, 0);

        AtomicInteger i = new AtomicInteger();
        tileEntity.getPoints().forEach((entity, points) -> {
            matrixStack.pushPose();
            matrixStack.translate(i.get() % 2 == 0 ? 0 : 1, 0.51 * i.get(), 0);

            builder.set(iRenderTypeBuffer.getBuffer(CustomRenderType.getHologram()));
            matrix4f.set(matrixStack.last().pose());

            builder.get().vertex(matrix4f.get(), i.get() % 2 != 0 ? 0.1F : 1.1F, 0.5F, 0).color(72, 153, 173, 255).endVertex();
            builder.get().vertex(matrix4f.get(), i.get() % 2 == 0 ? -0.1F : -1.1F, 0.5F, 0).color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(), Color.ORANGE.getBlue(), 255).endVertex();

            builder.get().vertex(matrix4f.get(), i.get() % 2 == 0 ? -0.1F : -1.1F, 0, 0).color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(), Color.ORANGE.getBlue(), 255).endVertex();
            builder.get().vertex(matrix4f.get(), i.get() % 2 != 0 ? 0.1F : 1.1F, 0, 0).color(72, 153, 173, 255).endVertex();

            builder.get().vertex(matrix4f.get(), i.get() % 2 == 0 ? -0.1F : -1.1F, 0.5F, 0).color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(), Color.ORANGE.getBlue(), 255).endVertex();
            builder.get().vertex(matrix4f.get(), i.get() % 2 != 0 ? 0.1F : 1.1F, 0.5F, 0).color(72, 153, 173, 255).endVertex();

            builder.get().vertex(matrix4f.get(), i.get() % 2 != 0 ? 0.1F : 1.1F, 0, 0).color(72, 153, 173, 255).endVertex();
            builder.get().vertex(matrix4f.get(), i.get() % 2 == 0 ? -0.1F : -1.1F, 0, 0).color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(), Color.ORANGE.getBlue(), 255).endVertex();


            matrixStack.translate(0, 0.05, 0);
            matrixStack.scale(0.2F, 0.2F, 0.01F);

            matrixStack.pushPose();
            matrixStack.translate(i.get() % 2 == 0 ? 4.75F : -4.75F, 1.5, 0.1);
            matrixStack.mulPose(new Quaternion(180,0,0,true));
            matrixStack.scale(0.1F,0.1F,0.1F);
            FontRenderer fontRenderer = Minecraft.getInstance().font;
            float f2 = (float) (-fontRenderer.width(points.toString()) / 2);
            matrix4f.set(matrixStack.last().pose());
            fontRenderer.drawInBatch(points.toString(), f2, 0, 16777215, false, matrix4f.get(), iRenderTypeBuffer, false, 255, light);
            matrixStack.popPose();

            matrixStack.mulPose(new Quaternion(0, i.get() % 2 == 0 ? 20 : -20, 0, true));
            matrixStack.pushPose();
            matrixStack.translate(i.get() % 2 == 0 ? 0.5 : -0.5, 0, -0.5);
            matrixStack.mulPose(new Quaternion(0, entity.yBodyRot, 0, true));
            Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).render(entity, 0, partialTicks, matrixStack, iRenderTypeBuffer, light);
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.translate(0, 0, 0.5);
            matrixStack.mulPose(new Quaternion(0, entity.yBodyRot, 0, true));
            Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).render(entity, 0, partialTicks, matrixStack, iRenderTypeBuffer, light);
            matrixStack.popPose();

            i.getAndIncrement();
            matrixStack.popPose();
        });

        matrixStack.popPose();
    }
}
