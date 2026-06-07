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

public class LauncherBEWLR extends BlockEntityWithoutLevelRenderer {
    public LauncherBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);

        if (stack.has(ModDataComponents.LAUNCHER_BEY))
            Minecraft.getInstance().getItemRenderer().renderStatic(stack.get(ModDataComponents.LAUNCHER_BEY).getStack(), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, null, 0);

    }
}
