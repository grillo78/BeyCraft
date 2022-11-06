package com.beycraft.client.book_component;

import com.beycraft.Beycraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class BigItemComponent  implements ICustomComponent {
    private transient int x, y;
    private transient ItemStack item;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
        Beycraft.LOGGER.debug("Custom Component Test built at ({}, {}) page {}", componentX, componentY, pageNum);
    }

    @Override
    public void render(MatrixStack matrixStack, IComponentRenderContext iComponentRenderContext, float v, int i, int i1) {
        matrixStack.pushPose();
        vazkii.patchouli.client.RenderHelper.transferMsToGl(matrixStack, () -> {
            renderGuiItem(item, x, y,Minecraft.getInstance().getItemRenderer().getModel(item, null, null));
        });
        matrixStack.popPose();
    }


    protected void renderGuiItem(ItemStack p_191962_1_, int p_191962_2_, int p_191962_3_, IBakedModel p_191962_4_) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        RenderSystem.pushMatrix();
        Minecraft.getInstance().textureManager.bind(AtlasTexture.LOCATION_BLOCKS);
        Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float)p_191962_2_, (float)p_191962_3_, 100.0F );
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(120.0F, 120.0F, 120.0F);
        MatrixStack matrixstack = new MatrixStack();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !p_191962_4_.usesBlockLight();
        if (flag) {
            RenderHelper.setupForFlatItems();
        }

        itemRenderer.render(p_191962_1_, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, p_191962_4_);
        irendertypebuffer$impl.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            RenderHelper.setupFor3DItems();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }

    @Override
    public boolean mouseClicked(IComponentRenderContext context, double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(lookup.apply(IVariable.wrap("#item")).asString())));
    }
}
