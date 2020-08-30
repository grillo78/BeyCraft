package com.grillo78.beycraft.items.render;

import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.items.ItemBeyLogger;
import com.grillo78.beycraft.items.ItemDualLauncher;
import com.grillo78.beycraft.items.ItemLauncherHandle;
import com.grillo78.beycraft.util.CachedStacks;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.HashMap;
import java.util.Random;

public class LauncherItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	private HashMap<CompoundNBT, ItemStack> stacks;

	public LauncherItemStackRendererTileEntity() {
		stacks = CachedStacks.INSTANCE.getStacks();
	}

	@Override
	public void func_239207_a_(ItemStack stack, TransformType transformType, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		super.func_239207_a_(stack, transformType, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
		matrixStack.push();
		matrixStack.scale(1.5f, 1.5f, 1.5f);
		matrixStack.translate(0.5, 0, 0);
		matrixStack.rotate(new Quaternion(new Vector3f(0, 1, 0), 90, true));
		if (stack.hasTag() && stack.getTag().contains("handle")) {
			matrixStack.scale(1.5f, 1.5f, 1.5f);
			matrixStack.rotate(new Quaternion(new Vector3f(1, 0, 0), 90, true));

			if (!stacks.containsKey(stack.getTag().get("handle"))) {
				stacks.put((CompoundNBT) stack.getTag().get("handle"),
						ItemStack.read((CompoundNBT) stack.getTag().get("handle")));
			}
			Minecraft.getInstance().getItemRenderer().renderItem(stacks.get(stack.getTag().get("handle")),
					TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
			matrixStack.translate(-0.08, 0.37, 0.06);
			matrixStack.rotate(new Quaternion(new Vector3f(1, 0, 0), -90, true));
			matrixStack.scale(0.5f, 0.5f, 0.5f);
		}
		if (stack.hasTag() && stack.getTag().contains("beylogger")) {
			matrixStack.scale(1.5f, 1.5f, 1.5f);
			matrixStack.rotate(new Quaternion(new Vector3f(1, 0, 0), 90, true));
			matrixStack.translate(0.09, -0.37, -0.08);
			if (!stacks.containsKey(stack.getTag().get("beylogger"))) {
				stacks.put((CompoundNBT) stack.getTag().get("beylogger"),
						ItemStack.read((CompoundNBT) stack.getTag().get("beylogger")));
			}
			Minecraft.getInstance().getItemRenderer().renderItem(stacks.get(stack.getTag().get("beylogger")),
					TransformType.FIXED, Integer.MAX_VALUE, combinedOverlayIn, matrixStack, buffer);
			matrixStack.translate(-0.09, 0.37, 0.08);
			matrixStack.rotate(new Quaternion(new Vector3f(1, 0, 0), -90, true));
			matrixStack.scale(0.5f, 0.5f, 0.5f);
		}
		matrixStack.translate(0, -0.05, 0);
		IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft",
				"launchers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/launcher_body"));

		IVertexBuilder vertexBuilder = buffer
				.getBuffer(RenderType.getEntityTranslucentCull(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
		for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
			if (stack.hasTag() && stack.getTag().contains("color")) {
				vertexBuilder.addVertexData(matrixStack.getLast(), quad,
						stack.getTag().getCompound("color").getFloat("red"),
						stack.getTag().getCompound("color").getFloat("green"),
						stack.getTag().getCompound("color").getFloat("blue"), combinedLightIn, combinedOverlayIn, true);
			} else {
				vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, combinedLightIn, combinedOverlayIn, true);
			}
		}
		if (stack.getItem() instanceof ItemDualLauncher) {
			if (stack.hasTag() && stack.getTag().contains("rotation") && stack.getTag().getInt("rotation") == 1) {
				matrixStack.translate(0, 0, -0.1);
			}
			model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft", "launchers/"
					+ stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/launcher_lever"));
			for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
				if (stack.hasTag() && stack.getTag().contains("color")) {
					vertexBuilder.addVertexData(matrixStack.getLast(), quad,
							stack.getTag().getCompound("color").getFloat("red"),
							stack.getTag().getCompound("color").getFloat("green"),
							stack.getTag().getCompound("color").getFloat("blue"), combinedLightIn, combinedOverlayIn, true);
				} else {
					vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, 1, 1, combinedOverlayIn, true);
				}
			}
		}
		for (PlayerEntity player : Minecraft.getInstance().world.getPlayers()) {
			if ((player.getHeldItem(Hand.MAIN_HAND) == stack || player.getHeldItem(Hand.OFF_HAND) == stack)
					&& player.getCooldownTracker().hasCooldown(stack.getItem())) {
				Matrix4f matrix4f1 = matrixStack.getLast().getMatrix();
				IVertexBuilder wr2 = buffer.getBuffer(RenderType.LINES);
				wr2.pos(matrix4f1, 0.05f, 0.215f, 0.35f).color(255, 255, 255, 255).endVertex();
				wr2.pos(matrix4f1, 0.05f, 0.215f, 2.2f).color(255, 255, 255, 255).endVertex();
				matrixStack.translate(0, 0, 1.75);
			}
		}
		model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft",
				"launchers/" + stack.getItem().getTranslationKey().replace("item.beycraft.", "") + "/grab_part"));
		vertexBuilder = buffer.getBuffer(RenderType.getEntityTranslucentCull(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
		for (BakedQuad quad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
			if (stack.hasTag() && stack.getTag().contains("color")) {
				vertexBuilder.addVertexData(matrixStack.getLast(), quad,
						stack.getTag().getCompound("color").getFloat("red"),
						stack.getTag().getCompound("color").getFloat("green"),
						stack.getTag().getCompound("color").getFloat("blue"), combinedLightIn, combinedOverlayIn, true);
			} else {
				vertexBuilder.addVertexData(matrixStack.getLast(), quad, 1, 1, 1, combinedLightIn, combinedOverlayIn, true);
			}
		}
		matrixStack.translate(0, 0.05, 0);
		matrixStack.rotate(new Quaternion(new Vector3f(1, 0, 0), 90, true));
		if (stack.hasTag() && stack.getTag().contains("bey")) {

			if (!stacks.containsKey(stack.getTag().get("bey"))) {
				stacks.put((CompoundNBT) stack.getTag().get("bey"),
						ItemStack.read((CompoundNBT) stack.getTag().get("bey")));
			}
			Minecraft.getInstance().getItemRenderer().renderItem(stacks.get(stack.getTag().get("bey")),
					TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
		}

		matrixStack.pop();
	}
}