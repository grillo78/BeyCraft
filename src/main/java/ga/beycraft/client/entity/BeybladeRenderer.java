package ga.beycraft.client.entity;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.math.Matrix4f;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.ModelInfo;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import ga.beycraft.client.util.CustomRenderType;
import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.item.LayerItem;
import ga.beycraft.common.item.ModItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

public class BeybladeRenderer extends EntityRenderer<BeybladeEntity> {

    public static final List<Runnable> RUNNABLES = Lists.newArrayList();
    public final List<RenderObject> childs = Lists.newArrayList();
    private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);

    public BeybladeRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(BeybladeEntity p_110775_1_) {
        return null;
    }

    @Override
    public void render(BeybladeEntity beyblade, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (beyblade.isAlive()) {

            matrixStack.pushPose();
            net.minecraft.util.math.vector.Matrix4f matrix4f1 = matrixStack.last().pose();
            LayerItem layerItem = (LayerItem) beyblade.getStack().getItem();
            LayerItem.Color color = layerItem.getResonanceColor();
            for (int i = 0; i < beyblade.getPoints().length; i++) {
                if (beyblade.getPoints()[i] != null) {
                    float x = (float) (beyblade.getPoints()[i].x - beyblade.getPosition(partialTicks).x);
                    float y = (float) (beyblade.getPoints()[i].y - beyblade.getPosition(partialTicks).y);
                    float z = (float) (beyblade.getPoints()[i].z - beyblade.getPosition(partialTicks).z);
                    if (i != beyblade.getPoints().length - 1) {
                        float x1 = (float) (beyblade.getPoints()[i + 1].x - beyblade.getPosition(partialTicks).x);
                        float y1 = (float) (beyblade.getPoints()[i + 1].y - beyblade.getPosition(partialTicks).y);
                        float z1 = (float) (beyblade.getPoints()[i + 1].z - beyblade.getPosition(partialTicks).z);
                        IVertexBuilder wr = bufferIn.getBuffer(CustomRenderType.THICKLINE);
                        wr.vertex(matrix4f1, x, y, z).color(color.getRed(), color.getGreen(), color.getBlue(), 80).endVertex();
                        wr.vertex(matrix4f1, x1, y1, z1).color(color.getRed(), color.getGreen(), color.getBlue(), 80).endVertex();
                        IVertexBuilder wr2 = bufferIn.getBuffer(CustomRenderType.THINLINE);
                        wr2.vertex(matrix4f1, x, y, z).color(255,255,255, 255).endVertex();
                        wr2.vertex(matrix4f1, x1, y1, z1).color(255,255,255, 255).endVertex();
                    } else {
                        IVertexBuilder wr2 = bufferIn.getBuffer(CustomRenderType.THINLINE);
                        wr2.vertex(matrix4f1, x, y, z).color(255,255,255, 255).endVertex();
                        wr2.vertex(matrix4f1, 0, 0, 0).color(255,255,255, 255).endVertex();
                        IVertexBuilder wr = bufferIn.getBuffer(CustomRenderType.THICKLINE);
                        wr.vertex(matrix4f1, x, y, z).color(color.getRed(), color.getGreen(), color.getBlue(), 80).endVertex();
                        wr.vertex(matrix4f1, 0, 0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 80).endVertex();
                    }
                }
            }
            matrixStack.popPose();

            beyblade.getStack().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
                RUNNABLES.add(() -> {
                    RenderLibRegistry.Compatibility.MODEL_VIEW_PROVIDER = () -> {
                        FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
                        matrixStack.last().pose().store(floatBuffer);

                        return new Matrix4f(floatBuffer);
                    };
                    ((LayerItem)beyblade.getStack().getItem()).renderBey(beyblade, layer, cap, partialTicks);
                });
            });
        }
    }
}
