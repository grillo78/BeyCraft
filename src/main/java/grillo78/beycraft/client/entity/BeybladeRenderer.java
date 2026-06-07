package grillo78.beycraft.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import grillo78.beycraft.entities.Beyblade;
import grillo78.beycraft.items.MainBeyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class BeybladeRenderer extends EntityRenderer<Beyblade> {

    public BeybladeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Beyblade entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        ItemStack beybladeStack = entity.getBeybladeItem();
        if (!beybladeStack.isEmpty()) {
            Vector3f beybladeScale = Minecraft.getInstance().getItemRenderer().getModel(beybladeStack, null, null, 0).getTransforms().ground.scale;
            MainBeyPart beyblade = (MainBeyPart) beybladeStack.getItem();
            poseStack.mulPose(Axis.YN.rotationDegrees(entity.getRotationAngle()));
            if (entity.getRotationSpeed() < 100)
                poseStack.mulPose(Axis.XN.rotationDegrees((100 - entity.getRotationSpeed())/100*30));
            poseStack.translate(0, beybladeScale.y * beyblade.getTotalHeight(beybladeStack), 0);
            Minecraft.getInstance().getItemRenderer().renderStatic(beybladeStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), 0);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Beyblade entity) {
        return null;
    }
}
