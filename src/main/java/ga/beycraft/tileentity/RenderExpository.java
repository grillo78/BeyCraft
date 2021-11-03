package ga.beycraft.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.entity.BeyRender;
import ga.beycraft.items.ItemBeyLayer;
import ga.beycraft.items.ItemBeyPart;
import ga.beycraft.util.ItemCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

@OnlyIn(value = Dist.CLIENT)
public class RenderExpository extends TileEntityRenderer<ExpositoryTileEntity> {

    public RenderExpository(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(ExpositoryTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
        matrixStack.pushPose();
        tileEntity.getInventory().ifPresent(h -> {
            if (h.getStackInSlot(0).getItem() instanceof ItemBeyPart) {
                matrixStack.translate(0.5,0.25,0.5);
                matrixStack.scale(1.6F,1.6F,1.6F);
                Minecraft.getInstance().getItemRenderer().renderStatic(h.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND, light, overlay, matrixStack, iRenderTypeBuffer);
            }
        });
        matrixStack.popPose();
    }
}
