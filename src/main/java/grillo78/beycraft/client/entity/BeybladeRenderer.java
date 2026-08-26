package grillo78.beycraft.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import grillo78.beycraft.blocks.StadiumBlock;
import grillo78.beycraft.client.render.MeshRegistry;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.entities.Beyblade;
import grillo78.beycraft.items.MainBeyPart;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import org.joml.Vector3f;

public class BeybladeRenderer extends EntityRenderer<Beyblade> {

    public static boolean RENDERING_CURRENT_STACK = false;
    public static float CURRENT_STACK_ALPHA = 1;

    public BeybladeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Beyblade entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        ItemStack beybladeStack = entity.getBeybladeItem();
        if (!beybladeStack.isEmpty()) {
            poseStack.pushPose();
            Vector3f beybladeScale = Minecraft.getInstance().getItemRenderer().getModel(beybladeStack, null, null, 0).getTransforms().ground.scale;
            MainBeyPart beyblade = (MainBeyPart) beybladeStack.getItem();
            poseStack.mulPose(Axis.YN.rotationDegrees(entity.getRotationAngle()));
            int startingWavleSpeed = 50;
            if (entity.getRotationSpeed() < startingWavleSpeed)
                poseStack.mulPose(Axis.XN.rotationDegrees(((startingWavleSpeed - entity.getRotationSpeed()) / startingWavleSpeed) * 30));
            poseStack.translate(0, beybladeScale.y * beyblade.getTotalHeight(beybladeStack), 0);
            Minecraft.getInstance().getItemRenderer().renderStatic(beybladeStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), 0);
            poseStack.popPose();

            if (entity.level().getBlockState(entity.getOnPos()).getBlock() instanceof StadiumBlock) {
//                Vec3 stadiumCenter = entity.getStadiumCenter();
//                Vec3 direction = stadiumCenter.subtract(entity.getPosition(partialTick));
//                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.LINES);
//                PoseStack.Pose pose = poseStack.last();
//                vertexConsumer.addVertex(pose, (float) 0, (float) 0, (float) 0)
//                        .setColor(1F, 1F, 0, 1F)
//                        .setNormal(pose, 0, 0, 0);
//                vertexConsumer.addVertex(pose, (float) direction.x, (float) direction.y, (float) direction.z)
//                        .setColor(1F, 1F, 0, 1F)
//                        .setNormal(pose, 0, 0, 0);
//                vertexConsumer.addVertex(pose, (float) direction.x, (float) direction.y + 0.5F, (float) direction.z + 1.2F)
//                        .setColor(1F, 1F, 0, 1F)
//                        .setNormal(pose, 0, 0, 0);
//                vertexConsumer.addVertex(pose, (float) direction.x, (float) direction.y + 0.5F, (float) direction.z)
//                        .setColor(1F, 1F, 0, 1F)
//                        .setNormal(pose, 0, 0, 0);
            }
            if (Minecraft.getInstance().hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult)Minecraft.getInstance().hitResult).getEntity() == entity && !FMLEnvironment.production){
//                Vec3 stadiumCenter = entity.getStadiumCenter();
//                Vec3 offsetToCenter = entity.position().subtract(stadiumCenter);
//                double desiredDistanceToCenter = 1.2 * entity.getRotationSpeed() / Beyblade.MAX_ROTATION_SPEED;
//                renderNameTag(entity, Component.literal("Desired distance to center: " + desiredDistanceToCenter), poseStack, bufferSource, packedLight, partialTick);
//                float radiusReduction = beyblade.getRadiusReduction(beybladeStack) * 0.015F + 0.01F;
//                poseStack.translate(0, 0.5, 0);
//                double distanceToCenter = offsetToCenter.length() - (desiredDistanceToCenter < offsetToCenter.length() ? radiusReduction : 1 - radiusReduction);
//                distanceToCenter = Math.clamp(distanceToCenter, 0.06F, desiredDistanceToCenter);
//                renderNameTag(entity, Component.literal("Actual distance to center: " + offsetToCenter.length()), poseStack, bufferSource, packedLight, partialTick);
//                poseStack.translate(0, 0.5, 0);
//                renderNameTag(entity, Component.literal("Moving to distance to center: " + distanceToCenter), poseStack, bufferSource, packedLight, partialTick);
            }
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Beyblade entity) {
        return null;
    }
}
