package com.grillo78.beycraft.items.render;

import com.grillo78.beycraft.util.CachedStacks;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.HashMap;
import java.util.Random;

public class BeyItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	private Random random = new Random();
	private HashMap<CompoundNBT, ItemStack> stacks;

	public BeyItemStackRendererTileEntity(){
		stacks = CachedStacks.INSTANCE.getStacks();
	}

	@Override
	public void func_239207_a_(ItemStack stack, TransformType transformType, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		super.func_239207_a_(stack, transformType, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
		matrixStack.push();

		IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("beycraft",
				"layers/" + stack.getItem().getRegistryName().getPath()));
		IVertexBuilder vertexBuilder = buffer
				.getBuffer(RenderType.getEntityTranslucentCull(PlayerContainer.LOCATION_BLOCKS_TEXTURE));
		MatrixStack.Entry entry = matrixStack.getLast();
		for (int i = 0; i < model.getQuads(null, null, random, EmptyModelData.INSTANCE).size(); i++) {
			vertexBuilder.addVertexData(entry, model.getQuads(null, null, random, EmptyModelData.INSTANCE).get(i), 1, 1, 1, 1, combinedLightIn,combinedOverlayIn, true);
		}
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			matrixStack.translate(0, 0, 0.5);
			matrixStack.scale(2, 2, 2);
			if (stack.hasTag() && stack.getTag().contains("chip")) {

				if (!stacks.containsKey(stack.getTag().get("chip"))) {
					stacks.put((CompoundNBT) stack.getTag().get("chip"),
							ItemStack.read((CompoundNBT) stack.getTag().get("chip")));
				}
				Minecraft.getInstance().getItemRenderer().renderItem(stacks.get(stack.getTag().get("chip")),
						TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);

				
			}
			if (stack.hasTag() && stack.getTag().contains("weight")) {
				matrixStack.rotate(new Quaternion(new Vector3f(0, 0, 1), 180, true));
				matrixStack.rotate(new Quaternion(new Vector3f(0, 1, 0), 45, true));
				matrixStack.translate(0.18, -0.05, 0.07);
				if (!stacks.containsKey(stack.getTag().get("weight"))) {
					stacks.put((CompoundNBT) stack.getTag().get("weight"),
							ItemStack.read((CompoundNBT) stack.getTag().get("weight")));
				}
				Minecraft.getInstance().getItemRenderer().renderItem(stacks.get(stack.getTag().get("weight")),
						TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
				
			}
			matrixStack.pop();
			matrixStack.push();
			if (stack.hasTag() && stack.getTag().contains("isEntity") && !stack.getTag().getBoolean("isEntity")) {
				matrixStack.scale(2F, 2F, 2F);
				matrixStack.rotate(new Quaternion(new Vector3f(0, 1, 0), -15, true));
				matrixStack.translate(0F, -0.04F, 0.25F);
				if (!stacks.containsKey(stack.getTag().get("disc"))) {
					stacks.put((CompoundNBT) stack.getTag().get("disc"),
							ItemStack.read((CompoundNBT) stack.getTag().get("disc")));
				}
				Minecraft.getInstance().getItemRenderer().renderItem(stacks.get(stack.getTag().get("disc")),
						TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
				matrixStack.rotate(new Quaternion(new Vector3f(0, 1, 0), 90, true));
				matrixStack.translate(0.25, -0.07, 0.25);
				if (!stacks.containsKey(stack.getTag().get("driver"))) {
					stacks.put((CompoundNBT) stack.getTag().get("driver"),
							ItemStack.read((CompoundNBT) stack.getTag().get("driver")));
				}
				Minecraft.getInstance().getItemRenderer().renderItem(stacks.get(stack.getTag().get("driver")),
						TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
			}
		});

		matrixStack.pop();
	}

}
