package grillo78.beycraft.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import grillo78.beycraft.client.render.MeshRegistry;
import grillo78.beycraft.client.render.ObjMesh;
import grillo78.beycraft.data.parts.Launcher;
import grillo78.beycraft.data.parts.LaunchersReloadListener;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LauncherBEWLR extends BlockEntityWithoutLevelRenderer {
    public LauncherBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);

        if(!stack.has(ModDataComponents.LAUNCHER) && LaunchersReloadListener.LAUNCHERS.containsKey(stack.get(ModDataComponents.LAUNCHER))) return;
        Launcher launcher = LaunchersReloadListener.LAUNCHERS.get(stack.get(ModDataComponents.LAUNCHER));
        ObjMesh mesh = MeshRegistry.get(launcher.getModel());
        if (mesh == null) return;
        poseStack.pushPose();
        mesh.render(poseStack, packedLight, 1, launcher.getTexture());
        poseStack.translate(0.5,0.5,0.5);
        switch (launcher.getGeneration()){
            case METAL:
                poseStack.mulPose(Axis.YN.rotationDegrees(-60));
                break;
        }
        if (stack.has(ModDataComponents.LAUNCHER_BEY))
            Minecraft.getInstance().getItemRenderer().renderStatic(stack.get(ModDataComponents.LAUNCHER_BEY).getStack(), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, null, 0);
        poseStack.popPose();
    }
}
