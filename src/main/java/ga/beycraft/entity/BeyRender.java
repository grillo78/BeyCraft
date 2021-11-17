package ga.beycraft.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.math.Matrix4f;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import ga.beycraft.items.ItemBeyDiscFrame;
import ga.beycraft.util.CustomRenderType;
import ga.beycraft.util.ItemCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.system.MemoryUtil;

import java.math.BigDecimal;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class BeyRender extends EntityRenderer<EntityBey> {
    private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);
    private static ArrayList<Runnable> runnables = new ArrayList<>();

    public static ArrayList<Runnable> getRunnables() {
        return runnables;
    }

    public BeyRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBey entity) {
        return null;
    }

    @Override
    public void render(EntityBey entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {
        net.minecraft.util.math.vector.Matrix4f matrix = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
        Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrix.invert();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        vector4f.transform(matrix);
        Vector3d pos = cameraPos.add(-vector4f.x(), -vector4f.y(), -vector4f.z());
        runnables.add(() -> {
            RenderLibRegistry.Compatibility.MODEL_VIEW_PROVIDER = () -> {
                FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
                matrixStack.last().pose().store(floatBuffer);

                return new Matrix4f(floatBuffer);
            };
            RenderObject sceneDriver = layer.addRenderObject(ItemCreator.models.get(entity.getDriver().getItem()));
            RenderObject sceneDisc = sceneDriver.addChild(ItemCreator.models.get(entity.getDisc().getItem()));
            RenderObject sceneFrame = null;
            ItemStack stack = entity.getDisc();
            RenderSystem.depthMask(true);
            if (stack.getItem() instanceof ItemBeyDiscFrame && stack.hasTag() && stack.getTag().contains("frame")) {
                Item discFrameItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("frame")).getString("id")));
                if (ItemCreator.models.get(discFrameItem) != null)
                    sceneFrame = sceneDisc.addChild(ItemCreator.models.get(discFrameItem));
            }
            RenderObject sceneLayer = sceneDisc.addChild(ItemCreator.models.get(entity.getLayer().getItem()));

            sceneDriver.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
            sceneDriver.transform.scale(0.5F, 0.5F, 0.5F);
            Vector3d vector = new Vector3d(1,0,0).yRot((float) Math.toRadians(MathHelper.lerp(partialTicks,entity.angle0, entity.angle)));
            if (entity.getRotationSpeed() > 1) {
                sceneDriver.transform.rotate((float) vector.x * (40 * MathHelper.lerp(partialTicks,entity.getRadius0(),entity.getRadius()) * -entity.getRotationDirection()), -MathHelper.lerp(partialTicks,entity.angle0, entity.angle) * 1.5f, (float) vector.z * (40 * MathHelper.lerp(partialTicks,entity.getRadius0(),entity.getRadius()) * -entity.getRotationDirection()));
            } else {
                sceneDriver.transform.rotate((float) vector.x * 30 * entity.getRotationDirection(), -entity.angle * 1.5f, (float) vector.z * 30 * entity.getRotationDirection());
            }
            sceneDriver.forceTransformUpdate();
            sceneDisc.forceTransformUpdate();
            if (sceneFrame != null) sceneFrame.forceTransformUpdate();
            sceneLayer.forceTransformUpdate();

            RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);
            sceneLayer.remove();
            if (sceneFrame != null) sceneFrame.remove();
            sceneDisc.remove();
            sceneDriver.remove();
        });
        if (this.entityRenderDispatcher.crosshairPickEntity == entity && !Minecraft.getInstance().player.isSpectator()
                && Minecraft.renderNames()) {
            matrixStack.pushPose();
            matrixStack.translate(0, 0.1F, 0);
            renderNameTag(entity, new StringTextComponent(entity.getPlayerName()), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.translate(0, -0.25F, 0);
            renderNameTag(entity, new StringTextComponent("Health: " + round(entity.getHealth() * 100 / entity.getMaxHealth(), 2) + "%"), matrixStack, bufferIn,
                    packedLightIn);
            matrixStack.popPose();
        }
        if (!entity.isDescending() && !entity.isStopped()) {
            net.minecraft.util.math.vector.Matrix4f matrix4f1 = matrixStack.last().pose();
            for (int i = 0; i < entity.getPoints().length; i++) {
                if (entity.getPoints()[i] != null) {
                    float x = (float) (entity.getPoints()[i].x - entity.position().x);
                    float y = (float) (entity.getPoints()[i].y - entity.position().y);
                    float z = (float) (entity.getPoints()[i].z - entity.position().z);
                    if (i != entity.getPoints().length - 1) {
                        float x1 = (float) (entity.getPoints()[i + 1].x - entity.position().x);
                        float y1 = (float) (entity.getPoints()[i + 1].y - entity.position().y);
                        float z1 = (float) (entity.getPoints()[i + 1].z - entity.position().z);
                        IVertexBuilder wr2 = bufferIn.getBuffer(CustomRenderType.THINLINE);
                        wr2.vertex(matrix4f1, x, y, z).color(255, 255, 0, 255).endVertex();
                        wr2.vertex(matrix4f1, x1, y1, z1).color(255, 255, 0, 255).endVertex();
                        IVertexBuilder wr = bufferIn.getBuffer(CustomRenderType.THICKLINE);
                        wr.vertex(matrix4f1, x, y, z).color(255, 255, 0, 80).endVertex();
                        wr.vertex(matrix4f1, x1, y1, z1).color(255, 255, 0, 80).endVertex();
                    } else {
                        IVertexBuilder wr2 = bufferIn.getBuffer(CustomRenderType.THINLINE);
                        wr2.vertex(matrix4f1, x, y, z).color(255, 255, 0, 255).endVertex();
                        wr2.vertex(matrix4f1, 0, 0, 0).color(255, 255, 0, 255).endVertex();
                        IVertexBuilder wr = bufferIn.getBuffer(CustomRenderType.THICKLINE);
                        wr.vertex(matrix4f1, x, y, z).color(255, 255, 0, 80).endVertex();
                        wr.vertex(matrix4f1, 0, 0, 0).color(255, 255, 0, 80).endVertex();
                    }
                }
            }
        }
    }

    public float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
