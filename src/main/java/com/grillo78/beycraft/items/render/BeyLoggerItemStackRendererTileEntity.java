package com.grillo78.beycraft.items.render;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

public class BeyLoggerItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	private HashMap<String, DynamicTexture> textures = new HashMap<>();
	private HashMap<String, ResourceLocation> textureLocations = new HashMap<>();
	private Random random = new Random();

	@Override
	public void render(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn,
			int combinedOverlayIn) {
		super.render(stack, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
		matrixStack.push();
		if (!ItemModels.MODELS.containsKey(stack.getItem().getTranslationKey())
				|| ItemModels.MODELS.get(stack.getItem().getTranslationKey()) == null) {

			ItemModels.MODELS.put(stack.getItem().getTranslationKey(),
					Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft",
							"beyloggers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "")));
		}
		IBakedModel model = ItemModels.MODELS.get(stack.getItem().getTranslationKey());
		IVertexBuilder vertexBuilder = buffer
				.getBuffer(RenderType.getEntityTranslucent(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
		for (BakedQuad quad : model.getQuads(null, null, random, EmptyModelData.INSTANCE)) {
			vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 0, combinedOverlayIn, true);
		}

		if (stack.hasTag() && stack.getTag().contains("url")
				&& !textures.containsKey(stack.getTag().getString("url"))) {
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					getTexture(stack.getTag().getString("url"));
				}
			});

			t1.start();
		}

		if (stack.hasTag() && stack.getTag().contains("url") && textures.containsKey(stack.getTag().getString("url"))) {
			matrixStack.rotate(new Quaternion(0, -90, 0, true));
			matrixStack.translate(-0.865, 0.47, -0.15);
			DynamicTexture texture = textures.get(stack.getTag().getString("url"));
			texture.bindTexture();
			IVertexBuilder builder = buffer
					.getBuffer(RenderType.getEntityTranslucent(textureLocations.get(stack.getTag().getString("url"))));
			Matrix4f posMatrix = matrixStack.getLast().getMatrix();
			Matrix3f normalMatrix = matrixStack.getLast().getNormal();
			builder.pos(posMatrix, 0, 0, 0).color(255, 255, 255, 255).tex(0, 0).overlay(combinedOverlayIn)
					.lightmap(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
			builder.pos(posMatrix, 0, 0, 0.29f).color(255, 255, 255, 255).tex(0, 0.79f).overlay(combinedOverlayIn)
					.lightmap(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
			builder.pos(posMatrix, 0.55f, 0, 0.29f).color(255, 255, 255, 255).tex(1, 0.79f).overlay(combinedOverlayIn)
					.lightmap(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
			builder.pos(posMatrix, 0.55f, 0, 0).color(255, 255, 255, 255).tex(1, 0).overlay(combinedOverlayIn)
					.lightmap(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();

			builder.pos(posMatrix, 0, 0, 0.29f).color(255, 255, 255, 255).tex(0, 0.79f).overlay(combinedOverlayIn)
					.lightmap(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
			builder.pos(posMatrix, 0, -0.04f, 0.339f).color(255, 255, 255, 255).tex(0, 1).overlay(combinedOverlayIn)
					.lightmap(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
			builder.pos(posMatrix, 0.55f, -0.04f, 0.339f).color(255, 255, 255, 255).tex(1, 1).overlay(combinedOverlayIn)
					.lightmap(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
			builder.pos(posMatrix, 0.55f, 0, 0.29f).color(255, 255, 255, 255).tex(1, 0.79f).overlay(combinedOverlayIn)
					.lightmap(combinedLightIn).normal(normalMatrix, 0, 1, 0).endVertex();
		}
		matrixStack.pop();
	}

	public void getTexture(String urlS) {
		try {
			URL url = new URL(urlS);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			NativeImage beyloggerImages = NativeImage.read(conn.getInputStream());
			DynamicTexture texture = new DynamicTexture(beyloggerImages);
			texture.updateDynamicTexture();
			textureLocations.put(urlS, new ResourceLocation("dynamic_texture_" + textureLocations.size()));
			Minecraft.getInstance().textureManager.loadTexture(textureLocations.get(urlS), texture);
			textures.put(urlS, texture);
		} catch (IOException e) {
			if (textures.containsKey(urlS)) {
				textures.remove(urlS);
			}
		}
	}
}