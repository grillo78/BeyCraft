package ga.beycraft.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import ga.beycraft.entity.BeyRender;
import ga.beycraft.items.ItemBeyDriver;
import ga.beycraft.items.ItemBeyPart;
import ga.beycraft.util.ItemCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class RenderBeyCreator extends TileEntityRenderer<BeyCreatorTileEntity> {

    private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);

    public RenderBeyCreator(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(BeyCreatorTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
        matrixStack.pushPose();

        matrixStack.translate(0.5, 0.14, 0.5);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        tileEntity.getInventory().ifPresent(h -> {
            if(h.getStackInSlot(0).getItem() instanceof ItemBeyDriver){
                matrixStack.mulPose(new Quaternion(180,0,0, true));
                matrixStack.translate(0,0,0.14);
            }
            if (h.getStackInSlot(0).getItem() instanceof ItemBeyPart) {
                net.minecraft.util.math.vector.Matrix4f matrix = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
                net.minecraft.util.math.vector.Matrix4f modelView = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
                Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                matrix.invert();
                Vector4f vector4f = new Vector4f(0, 0, 0, 1/16);
                vector4f.transform(matrix);
                Vector3d pos = cameraPos.add(-vector4f.x(), -vector4f.y(), -vector4f.z());
                BeyRender.getRunnables().add(() -> {
                    RenderLibRegistry.Compatibility.MODEL_VIEW_PROVIDER = () -> {
                        FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
                        modelView.store(floatBuffer);

                        return new friedrichlp.renderlib.math.Matrix4f(floatBuffer);
                    };
                    RenderObject scenePart = layer.addRenderObject(ItemCreator.models.get(h.getStackInSlot(0).getItem()));

                    scenePart.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
                    scenePart.forceTransformUpdate();
                    RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);
                    layer.removeRenderObject(scenePart);

                });
            }else
                Minecraft.getInstance().getItemRenderer().renderStatic(h.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED, light, overlay, matrixStack, iRenderTypeBuffer);
        });
        matrixStack.popPose();
    }
}
