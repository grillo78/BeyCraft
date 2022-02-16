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
import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.items.ItemBeyDiscFrame;
import ga.beycraft.items.ItemClearWheel;
import ga.beycraft.util.CustomRenderType;
import ga.beycraft.util.ItemCreator;
import ga.beycraft.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.system.MemoryUtil;

import java.math.BigDecimal;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

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
            RenderObject mainScene = layer.addRenderObject(ItemCreator.models.get(entity.getDriver().getItem()));
            try {
                mainScene.setHidden(true);
            } catch (NullPointerException e) {
            }
            RenderObject sceneDriver = mainScene.addChild(ItemCreator.models.get(entity.getDriver().getItem()));
            RenderObject sceneDisc = sceneDriver.addChild(ItemCreator.models.get(entity.getDisc().getItem()));
            RenderObject sceneFrame = null;
            ItemStack stack = entity.getDisc();
            RenderSystem.depthMask(true);
            if (stack.getItem() instanceof ItemBeyDiscFrame && stack.hasTag() && stack.getTag().contains("frame")) {
                Item discFrameItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("frame")).getString("id")));
                if (ItemCreator.models.get(discFrameItem) != null) {
                    sceneFrame = sceneDisc.addChild(ItemCreator.models.get(discFrameItem));
                    sceneFrame.transform.rotate(0, ((ItemBeyDiscFrame) stack.getItem()).getFrameRotation(), 0);
                }
            }
            RenderObject sceneLayer = sceneDisc.addChild(ItemCreator.models.get(entity.getLayer().getItem()));
            RenderObject sceneClearWheel = null;
            stack = entity.getLayer();
            if(stack.getItem() instanceof ItemClearWheel && stack.hasTag() && stack.getTag().contains("FusionWheel")) {
                Item fusionWheelItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("FusionWheel")).getString("id")));
                sceneClearWheel = sceneLayer.addChild(ItemCreator.models.get(fusionWheelItem));
            }

            mainScene.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
            mainScene.transform.scale(0.5F, 0.5F, 0.5F);
            float angle = entity.isStopped() ? entity.getAngle() : MathHelper.lerp(partialTicks, entity.getAngle0(), entity.getAngle());
            sceneDriver.transform.rotate(0,
                    -angle,
                    0);
            sceneLayer.transform.rotate(0, entity.getRotationDirection() * (1 - entity.getHealth() / entity.getMaxHealth()) * -72, 0);
            Vector3d vector = new Vector3d(1, 0, 0).yRot((float) Math.toRadians(entityYaw - 45));
            if (entity.getRotationSpeed() > 1) {
                float inclination = 40 * MathHelper.lerp(partialTicks, entity.getRadius0(), entity.getRadius());
                mainScene.transform.rotate((float) -vector.x * inclination * entity.getRotationDirection(), 0, (float) vector.z * inclination * entity.getRotationDirection());
            } else {
                mainScene.transform.rotate((float) vector.x * 30 * entity.getRotationDirection(), 0, (float) vector.z * 30 * entity.getRotationDirection());
            }
            mainScene.forceTransformUpdate();
            sceneDriver.forceTransformUpdate();
            sceneDisc.forceTransformUpdate();
            if (sceneFrame != null) sceneFrame.forceTransformUpdate();
            sceneLayer.forceTransformUpdate();

            RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);
            if (sceneClearWheel != null) sceneClearWheel.remove();
            sceneLayer.remove();
            if (sceneFrame != null) sceneFrame.remove();
            sceneDisc.remove();
            sceneDriver.remove();
            mainScene.remove();
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

        if (entity.getLightnings() != null) {
            PlayerEntity player = PlayerUtils.getPlayerByName(entity.getPlayerName(), entity.level);
            if (player != null)
                player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
                    if (h.isInResonance() && !entity.isStopped())
                        for (int i = 0; i < entity.getLightnings().length; i++) {
                            matrixStack.pushPose();
                            EntityBey.Lightning lightning = entity.getLightnings()[i];
                            matrixStack.translate(0, 0.1, 0);
                            matrixStack.scale(0.002F, 0.002F * (0.5F + new Random(lightning.getSeed()).nextFloat() / 2), 0.002F);
                            Random random = new Random(lightning.getSeed());
                            matrixStack.mulPose(new Quaternion(random.nextBoolean() ? random.nextFloat() * 20 : random.nextFloat() * -20, random.nextFloat() * 360, random.nextInt(360), true));
                            renderLightning(lightning, bufferIn, matrixStack);
                            matrixStack.popPose();
                        }
                });
        }
        net.minecraft.util.math.vector.Matrix4f matrix4f1 = matrixStack.last().pose();

        for (int i = 0; i < entity.getPoints().length; i++) {
            if (entity.getPoints()[i] != null) {
                float x = (float) (entity.getPoints()[i].x - entity.getPosition(partialTicks).x);
                float y = (float) (entity.getPoints()[i].y - entity.getPosition(partialTicks).y);
                float z = (float) (entity.getPoints()[i].z - entity.getPosition(partialTicks).z);
                if (i != entity.getPoints().length - 1) {
                    float x1 = (float) (entity.getPoints()[i + 1].x - entity.getPosition(partialTicks).x);
                    float y1 = (float) (entity.getPoints()[i + 1].y - entity.getPosition(partialTicks).y);
                    float z1 = (float) (entity.getPoints()[i + 1].z - entity.getPosition(partialTicks).z);
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

    private void renderLightning(EntityBey.Lightning lightning, IRenderTypeBuffer bufferIn, MatrixStack stack) {
        float[] afloat = new float[8];
        float[] afloat1 = new float[8];
        float f = 0.0F;
        float f1 = 0.0F;
        Random random = new Random(lightning.getSeed());

        for (int i = 7; i >= 0; --i) {
            afloat[i] = f;
            afloat1[i] = f1;
            f += (float) (random.nextInt(11) - 5);
            f1 += (float) (random.nextInt(11) - 5);
        }

        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.lightning());
        net.minecraft.util.math.vector.Matrix4f matrix4f = stack.last().pose();

        for (int j = 0; j < 4; ++j) {
            Random random1 = new Random(0);

            for (int k = 0; k < 3; ++k) {
                int l = 7;
                int i1 = 0;
                if (k > 0) {
                    l = 7 - k;
                }

                if (k > 0) {
                    i1 = l - 2;
                }

                float f2 = afloat[l] - f;
                float f3 = afloat1[l] - f1;

                for (int j1 = l; j1 >= i1; --j1) {
                    float f4 = f2;
                    float f5 = f3;
                    if (k == 0) {
                        f2 += (float) (random1.nextInt(11) - 5);
                        f3 += (float) (random1.nextInt(11) - 5);
                    } else {
                        f2 += (float) (random1.nextInt(31) - 15);
                        f3 += (float) (random1.nextInt(31) - 15);
                    }

                    float f10 = 0.1F + (float) j * 0.2F;
                    if (k == 0) {
                        f10 = (float) ((double) f10 * ((double) j1 * 0.1D + 1.0D));
                    }

                    float f11 = 0.1F + (float) j * 0.2F;
                    if (k == 0) {
                        f11 *= (float) (j1 - 1) * 0.1F + 1.0F;
                    }

                    quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, lightning.getRed(), lightning.getGreen(), lightning.getBlue(), f10, f11, false, false, true, false);
                    quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, lightning.getRed(), lightning.getGreen(), lightning.getBlue(), f10, f11, true, false, true, true);
                    quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, lightning.getRed(), lightning.getGreen(), lightning.getBlue(), f10, f11, true, true, false, true);
                    quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, lightning.getRed(), lightning.getGreen(), lightning.getBlue(), f10, f11, false, true, false, false);
                }
            }
        }
    }

    private static void quad(net.minecraft.util.math.vector.Matrix4f p_229116_0_, IVertexBuilder p_229116_1_, float p_229116_2_, float p_229116_3_, int p_229116_4_, float p_229116_5_, float p_229116_6_, float p_229116_7_, float p_229116_8_, float p_229116_9_, float p_229116_10_, float p_229116_11_, boolean p_229116_12_, boolean p_229116_13_, boolean p_229116_14_, boolean p_229116_15_) {
        p_229116_1_.vertex(p_229116_0_, p_229116_2_ + (p_229116_12_ ? p_229116_11_ : -p_229116_11_), (float) (p_229116_4_ * 16), p_229116_3_ + (p_229116_13_ ? p_229116_11_ : -p_229116_11_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 0.3F).endVertex();
        p_229116_1_.vertex(p_229116_0_, p_229116_5_ + (p_229116_12_ ? p_229116_10_ : -p_229116_10_), (float) ((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_13_ ? p_229116_10_ : -p_229116_10_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 0.3F).endVertex();
        p_229116_1_.vertex(p_229116_0_, p_229116_5_ + (p_229116_14_ ? p_229116_10_ : -p_229116_10_), (float) ((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_15_ ? p_229116_10_ : -p_229116_10_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 0.3F).endVertex();
        p_229116_1_.vertex(p_229116_0_, p_229116_2_ + (p_229116_14_ ? p_229116_11_ : -p_229116_11_), (float) (p_229116_4_ * 16), p_229116_3_ + (p_229116_15_ ? p_229116_11_ : -p_229116_11_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 0.3F).endVertex();
    }

    public float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
