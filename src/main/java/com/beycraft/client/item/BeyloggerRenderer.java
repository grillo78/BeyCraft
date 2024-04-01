package com.beycraft.client.item;

import com.beycraft.Beycraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

public class BeyloggerRenderer extends ItemStackTileEntityRenderer {

    private HashMap<String, DynamicTexture> textures = new HashMap<>();
    private HashMap<String, ResourceLocation> textureLocations = new HashMap<>();
    private HashMap<String, NativeImage> textureImages = new HashMap<>();
    private Random random = new Random();

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
                             IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.pushPose();
        IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(Beycraft.MOD_ID,
                "beyloggers/" + stack.getItem().getRegistryName().getPath()));
        IVertexBuilder vertexBuilder = buffer
                .getBuffer(RenderType.entitySolid(AtlasTexture.LOCATION_BLOCKS));
        MatrixStack.Entry entry = matrixStack.last();
        for (int i = 0; i < model.getQuads(null, null, random, EmptyModelData.INSTANCE).size(); i++) {
            vertexBuilder.addVertexData(entry, model.getQuads(null, null, random, EmptyModelData.INSTANCE).get(i), 1, 1, 1, 1, combinedLightIn, combinedOverlayIn, true);
        }

        if (stack.hasTag() && stack.getTag().contains("url")
                && !textures.containsKey(stack.getTag().getString("url"))) {
            if (!textureImages.containsKey(stack.getTag().getString("url"))) {
                Thread t1 = new Thread(() -> {
                    downloadTexture(stack.getTag().getString("url"));
                });
                t1.start();
            } else {
                getTexture(stack.getTag().getString("url"));
            }

        }

        if (stack.hasTag() && stack.getTag().contains("url") && textures.containsKey(stack.getTag().getString("url"))) {
            matrixStack.mulPose(new Quaternion(0, -90, 0, true));
            matrixStack.translate(-0.865, 0.47, -0.15);
            DynamicTexture texture = textures.get(stack.getTag().getString("url"));
            texture.bind();
            IVertexBuilder builder = buffer
                    .getBuffer(RenderType.entityTranslucent(textureLocations.get(stack.getTag().getString("url"))));
            Matrix4f posMatrix = matrixStack.last().pose();
            Matrix3f normalMatrix = matrixStack.last().normal();
            builder.vertex(posMatrix, 0, 0, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(combinedOverlayIn)
                    .uv2(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
            builder.vertex(posMatrix, 0, 0, 0.29f).color(255, 255, 255, 255).uv(0, 0.79f).overlayCoords(combinedOverlayIn)
                    .uv2(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
            builder.vertex(posMatrix, 0.55f, 0, 0.29f).color(255, 255, 255, 255).uv(1, 0.79f).overlayCoords(combinedOverlayIn)
                    .uv2(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
            builder.vertex(posMatrix, 0.55f, 0, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(combinedOverlayIn)
                    .uv2(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();

            builder.vertex(posMatrix, 0, 0, 0.29f).color(255, 255, 255, 255).uv(0, 0.79f).overlayCoords(combinedOverlayIn)
                    .uv2(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
            builder.vertex(posMatrix, 0, -0.04f, 0.339f).color(255, 255, 255, 255).uv(0, 1).overlayCoords(combinedOverlayIn)
                    .uv2(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
            builder.vertex(posMatrix, 0.55f, -0.04f, 0.339f).color(255, 255, 255, 255).uv(1, 1).overlayCoords(combinedOverlayIn)
                    .uv2(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
            builder.vertex(posMatrix, 0.55f, 0, 0.29f).color(255, 255, 255, 255).uv(1, 0.79f).overlayCoords(combinedOverlayIn)
                    .uv2(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
        }
        matrixStack.popPose();
    }

    private void downloadTexture(String urlS) {
        try {
            URL url = new URL(urlS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            NativeImage beyloggerImages = NativeImage.read(conn.getInputStream());
            textureImages.put(urlS, beyloggerImages);
        } catch (Exception e) {
            if (textureImages.containsKey(urlS)) {
                textureImages.remove(urlS);
            }
        }
    }

    public void getTexture(String urlS) {
        try {
            NativeImage beyloggerImages = textureImages.get(urlS);
            DynamicTexture texture = new DynamicTexture(beyloggerImages);
            textureLocations.put(urlS, new ResourceLocation("dynamic_texture_" + textureLocations.size()));
            Minecraft.getInstance().textureManager.register(textureLocations.get(urlS), texture);
            textures.put(urlS, texture);
        } catch (Exception e) {
            if (textures.containsKey(urlS)) {
                textures.remove(urlS);
            }
        }
    }
}
