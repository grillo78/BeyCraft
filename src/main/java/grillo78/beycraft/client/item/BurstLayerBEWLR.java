package grillo78.beycraft.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.client.BeycraftClient;
import grillo78.beycraft.client.render.MeshRegistry;
import grillo78.beycraft.client.render.ObjMesh;
import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class BurstLayerBEWLR extends BlockEntityWithoutLevelRenderer {
    public BurstLayerBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        Beypart beypart = BeypartsReloadListener.BEYPARTS.get(stack.get(ModDataComponents.BEYPART));
        if(beypart == null) return;
        ObjMesh mesh = MeshRegistry.get(beypart.getModel());
        if (mesh == null) return;
        poseStack.pushPose();
        mesh.render(poseStack, packedLight, 1);
        poseStack.popPose();
    }
}
