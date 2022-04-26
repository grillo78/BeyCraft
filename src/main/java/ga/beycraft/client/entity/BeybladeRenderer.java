package ga.beycraft.client.entity;

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
