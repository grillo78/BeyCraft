package grillo78.beycraft.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import grillo78.beycraft.client.render.MeshRegistry;
import grillo78.beycraft.client.render.ObjMesh;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.data.parts.metal.SpinTrack;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MetalEnergyRingBEWLR extends BlockEntityWithoutLevelRenderer {
    public MetalEnergyRingBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        Beypart beypart = BeypartsReloadListener.BEYPARTS.get(stack.get(ModDataComponents.BEYPART));
        if (beypart == null) return;
        ObjMesh mesh = MeshRegistry.get(beypart.getModel());
        if (mesh == null) return;
        poseStack.pushPose();
        mesh.renderBeyPart(poseStack, packedLight);
        poseStack.translate(0.5, 0.5, 0.5);
        if (stack.has(ModDataComponents.METAL_FACEBOLT))
            Minecraft.getInstance().getItemRenderer().renderStatic(stack.get(ModDataComponents.METAL_FACEBOLT).getStack(), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, null, 0);
        if (stack.has(ModDataComponents.METAL_FUSION_WHEEL)) {
            Minecraft.getInstance().getItemRenderer().renderStatic(stack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack(), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, null, 0);
            if (stack.has(ModDataComponents.METAL_SPIN_TRACK)) {
                Minecraft.getInstance().getItemRenderer().renderStatic(stack.get(ModDataComponents.METAL_SPIN_TRACK).getStack(), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, null, 0);
                if (stack.has(ModDataComponents.METAL_PERFORMANCE_TIP)) {
                    poseStack.pushPose();
                    SpinTrack spinTrack = (SpinTrack) BeypartsReloadListener.BEYPARTS.get(stack.get(ModDataComponents.METAL_SPIN_TRACK).getStack().get(ModDataComponents.BEYPART));
                    if(spinTrack != null)poseStack.translate(0, -spinTrack.getHeight(), 0);
                    Minecraft.getInstance().getItemRenderer().renderStatic(stack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack(), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, null, 0);
                    poseStack.popPose();
                }
            }
        }
        poseStack.popPose();
    }
}
