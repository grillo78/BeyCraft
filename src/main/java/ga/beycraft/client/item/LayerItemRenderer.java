package ga.beycraft.client.item;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import ga.beycraft.client.util.BeyPartModel;
import ga.beycraft.common.item.DiscFrameItem;
import ga.beycraft.common.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

public class LayerItemRenderer extends BlockEntityWithoutLevelRenderer {
    private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);
    public LayerItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource bufferSource, int p_108834_, int p_108835_) {
        super.renderByItem(stack, transformType, matrixStack, bufferSource, p_108834_, p_108835_);
        matrixStack.pushPose();
        Matrix4f matrix = new Matrix4f(matrixStack.last().pose());
        Matrix4f modelView = new Matrix4f(matrixStack.last().pose());
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrix.invert();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        vector4f.transform(matrix);
        Vec3 pos = cameraPos.add(vector4f.x(), vector4f.y(), vector4f.z());
        RenderObject sceneLayer;
        BeyPartModel model = null;
        switch (transformType) {
            case GUI:
                sceneLayer = layer.addRenderObject(ModItems.ItemCreator.models.get(stack.getItem()));
                break;
            case FIRST_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
                vector4f = new Vector4f(0, 0, 0, 1 / 16);
                vector4f.transform(matrix);
                pos = cameraPos.add(vector4f.x(), vector4f.y() + 0.2, vector4f.z());
                model = new BeyPartModel(modelView, pos, ModItems.ItemCreator.models.get(stack.getItem()), 20, 180, 0, 0.5F, 0.5F, 0.5F);
                sceneLayer = model.getSceneLayer();
                BeyPartModel.handModels.add(model);
                break;
            case GROUND:
            case THIRD_PERSON_RIGHT_HAND:
            case THIRD_PERSON_LEFT_HAND:
                vector4f = new Vector4f(0, 0, 0, 1 / 16);
                vector4f.transform(matrix);
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                matrixStack.translate(0.5F, 0.5F, 0.5F);
                modelView = new com.mojang.math.Matrix4f(matrixStack.last().pose());
                pos = cameraPos.add(vector4f.x(), vector4f.y(), vector4f.z());
            default:
                model = new BeyPartModel(modelView, pos, ModItems.ItemCreator.models.get(stack.getItem()), 0, 0, 0, 1, 1, 1);
                sceneLayer = model.getSceneLayer();
                BeyPartModel.worldModels.add(model);
                break;
        }
        if (model != null) {
            if (stack.hasTag() && stack.getTag().contains("frame")) {
                Item frameItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundTag) stack.getTag().get("frame")).getString("id")));
                if (ModItems.ItemCreator.models.containsKey(frameItem) && stack.getItem() instanceof DiscFrameItem) {
                    RenderObject sceneFrame = model.addChild(ModItems.ItemCreator.models.get(frameItem));
                    sceneFrame.transform.rotate(0,((DiscFrameItem)stack.getItem()).getFrameRotation(),0);
                }
            }
        } else {
//            GL11.glRotatef(50, 1, 0, 0);
//            GL11.glTranslated(0, -0.5, 0);
//            GL11.glScaled(2, 2, 2);
            vector4f = new Vector4f(0, 0, 0, 16);
            vector4f.transform(matrix);
            pos = cameraPos.add(vector4f.x(), vector4f.y(), vector4f.z());

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            sceneLayer.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
            sceneLayer.forceTransformUpdate();
            RenderManager.render(layer, RenderMode.USE_FFP_MATS);
            sceneLayer.remove();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
        matrixStack.popPose();
    }
}
