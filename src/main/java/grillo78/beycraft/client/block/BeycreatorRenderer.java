package grillo78.beycraft.client.block;

import grillo78.beycraft.common.block.BeycreatorBlock;
import grillo78.beycraft.common.block_entity.BeycreatorTileEntity;
import grillo78.beycraft.common.item.BeyPartItem;
import grillo78.beycraft.common.item.ModItems;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.math.Matrix4f;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.ModelInfo;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

public class BeycreatorRenderer extends TileEntityRenderer<BeycreatorTileEntity> {

    public static final List<Runnable> RUNNABLES = Lists.newArrayList();
    private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);
    private float rotation = 0;

    public BeycreatorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(BeycreatorTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Vector3d pos;
        net.minecraft.util.math.vector.Matrix4f matrix;
        net.minecraft.util.math.vector.Matrix4f modelView;
        Vector3d cameraPos;
        Vector4f vector4f;
        if (BeyPartItem.getParts().get(tileEntityIn.getIndex()) instanceof BeyPartItem) {
            matrixStack.pushPose();

            rotation += 0.1;

            Quaternion quaternion;

            switch (tileEntityIn.getBlockState().getValue(BeycreatorBlock.FACING)) {
                case NORTH:
                    matrixStack.translate(0.875, 0.2, 0.175);
                    quaternion = new Quaternion(0, 0, 0, true);
                    break;
                case EAST:
                    matrixStack.translate(0.825, 0.2, 0.875);
                    quaternion = new Quaternion(0, -90, 0, true);
                    break;
                case SOUTH:
                    matrixStack.translate(0.125, 0.2, 0.825);
                    quaternion = new Quaternion(0, -180, 0, true);
                    break;
                case WEST:
                    matrixStack.translate(0.175, 0.2, 0.125);
                    quaternion = new Quaternion(0, -270, 0, true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + tileEntityIn.getBlockState().getValue(BeycreatorBlock.FACING));
            }


            matrixStack.mulPose(quaternion);
            matrix = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
            cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            modelView = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
            matrix.invert();
            vector4f = new Vector4f(0, 0, 0, 1 / 16);
            vector4f.transform(matrix);
            pos = cameraPos.add(-vector4f.x(), -vector4f.y(), -vector4f.z());
            net.minecraft.util.math.vector.Matrix4f finalModelView = modelView;
            Vector3d finalPos = pos;
            RUNNABLES.add(() -> {
                RenderLibRegistry.Compatibility.MODEL_VIEW_PROVIDER = () -> {
                    FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
                    finalModelView.store(floatBuffer);

                    return new Matrix4f(floatBuffer);
                };


                RenderObject hologramScene = layer.addRenderObject(getModelInfo(BeyPartItem.getParts().get(tileEntityIn.getIndex())));

                RenderSystem.depthMask(true);
                hologramScene.transform.setPosition((float) finalPos.x, (float) finalPos.y, (float) finalPos.z);
                hologramScene.transform.scale(0.3F, 0.3F, 0.3F);
                hologramScene.transform.rotate(0, rotation, 0);
                hologramScene.forceTransformUpdate();

                RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);

                hologramScene.remove();
            });

            matrixStack.popPose();
        }
        matrixStack.pushPose();

        matrixStack.translate(0.5, 0.1, 0.5);
        if (tileEntityIn.getSlot().getItem() instanceof BeyPartItem) {
            matrix = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
            cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            modelView = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
            matrix.invert();
            vector4f = new Vector4f(0, 0, 0, 1 / 16);
            vector4f.transform(matrix);
            pos = cameraPos.add(-vector4f.x(), -vector4f.y(), -vector4f.z());
            net.minecraft.util.math.vector.Matrix4f finalModelView1 = modelView;
            Vector3d finalPos1 = pos;
            RUNNABLES.add(() -> {
                RenderLibRegistry.Compatibility.MODEL_VIEW_PROVIDER = () -> {
                    FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
                    finalModelView1.store(floatBuffer);

                    return new Matrix4f(floatBuffer);
                };


                RenderObject hologramScene = layer.addRenderObject(getModelInfo(tileEntityIn.getSlot().getItem()));

                RenderSystem.depthMask(true);
                hologramScene.transform.setPosition((float) finalPos1.x, (float) finalPos1.y, (float) finalPos1.z);
                hologramScene.transform.scale(0.3F, 0.3F, 0.3F);
                hologramScene.forceTransformUpdate();

                RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);

                hologramScene.remove();
            });
        } else {
            matrixStack.translate(0,0.05,0);
            matrixStack.mulPose(new Quaternion(90,0,0,true));
            matrixStack.scale(0.2F,0.2F,0.2F);
            Minecraft.getInstance().getItemRenderer().renderStatic(tileEntityIn.getSlot(), ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
        }
        matrixStack.popPose();
    }

    private ModelInfo getModelInfo(Item item) {
        return ModItems.ItemCreator.MODELS.get(item);
    }
}
