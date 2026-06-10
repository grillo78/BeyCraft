package grillo78.beycraft.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import grillo78.beycraft.client.render.MeshRegistry;
import grillo78.beycraft.client.render.ObjMesh;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BurstLayerBEWLR extends BlockEntityWithoutLevelRenderer {
    public BurstLayerBEWLR() {
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
        poseStack.translate(0.5,0.5,0.5);
        if (stack.has(ModDataComponents.BURST_DISC))
            Minecraft.getInstance().getItemRenderer().renderStatic(stack.get(ModDataComponents.BURST_DISC).getStack(), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, null, 0);
        if (stack.has(ModDataComponents.BURST_DRIVER))
            Minecraft.getInstance().getItemRenderer().renderStatic(stack.get(ModDataComponents.BURST_DRIVER).getStack(), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, null, 0);
        poseStack.popPose();
    }
}
