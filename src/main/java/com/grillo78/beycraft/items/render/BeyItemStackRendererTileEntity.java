package com.grillo78.beycraft.items.render;

import com.grillo78.beycraft.util.CachedStacks;
import com.grillo78.beycraft.util.ItemCreator;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.math.Matrix4f;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BeyItemStackRendererTileEntity extends ItemStackTileEntityRenderer {

	private Random random = new Random();
	private HashMap<CompoundNBT, ItemStack> stacks;
	private RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);
	private static ArrayList<Runnable> runnables = new ArrayList<>();

	public static ArrayList<Runnable> getRunnables() {
		return runnables;
	}

	public BeyItemStackRendererTileEntity(){
		stacks = CachedStacks.INSTANCE.getStacks();
	}

	@Override
	public void renderByItem(ItemStack stack, TransformType transformType, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		super.renderByItem(stack, transformType, matrixStack, buffer, combinedLightIn, combinedOverlayIn);


		net.minecraft.util.math.vector.Matrix4f matrix = new net.minecraft.util.math.vector.Matrix4f(matrixStack.last().pose());
		Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		matrix.invert();
		Vector4f vector4f = new Vector4f(0,0,0,1);
		vector4f.transform(matrix);
		Vector3d pos = cameraPos.add(-vector4f.x(),-vector4f.y(),-vector4f.z());
		runnables.add(() -> {
			RenderLibRegistry.Compatibility.MODEL_VIEW_PROVIDER = () -> {
				FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
				matrixStack.last().pose().store(floatBuffer);

				return new Matrix4f(floatBuffer);
			};
			RenderObject sceneLayer = layer.addRenderObject(ItemCreator.models.get(stack.getItem()));

			if(sceneLayer != null) {
				sceneLayer.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
				sceneLayer.transform.scale(0.5F, 0.5F, 0.5F);
				sceneLayer.forceTransformUpdate();
				RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);
			}
			layer.removeRenderObject(sceneLayer);
		});

		matrixStack.pushPose();
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			matrixStack.translate(0, 0, 0.5);
			matrixStack.scale(2, 2, 2);
			if (stack.hasTag() && stack.getTag().contains("chip")) {

				if (!stacks.containsKey(stack.getTag().get("chip"))) {
					stacks.put((CompoundNBT) stack.getTag().get("chip"),
							ItemStack.of((CompoundNBT) stack.getTag().get("chip")));
				}
				Minecraft.getInstance().getItemRenderer().renderStatic(stacks.get(stack.getTag().get("chip")),
						TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);

				
			}
			if (stack.hasTag() && stack.getTag().contains("weight")) {
				matrixStack.mulPose(new Quaternion(new Vector3f(0, 0, 1), 180, true));
				matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), 45, true));
				matrixStack.translate(0.18, -0.05, 0.07);
				if (!stacks.containsKey(stack.getTag().get("weight"))) {
					stacks.put((CompoundNBT) stack.getTag().get("weight"),
							ItemStack.of((CompoundNBT) stack.getTag().get("weight")));
				}
				Minecraft.getInstance().getItemRenderer().renderStatic(stacks.get(stack.getTag().get("weight")),
						TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
				
			}
			matrixStack.popPose();
			matrixStack.pushPose();
			if (stack.hasTag() && stack.getTag().contains("isEntity") && !stack.getTag().getBoolean("isEntity")) {
				matrixStack.scale(2F, 2F, 2F);
				matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), -15, true));
				matrixStack.translate(0F, -0.04F, 0.25F);
				if (!stacks.containsKey(stack.getTag().get("disc"))) {
					stacks.put((CompoundNBT) stack.getTag().get("disc"),
							ItemStack.of((CompoundNBT) stack.getTag().get("disc")));
				}
				Minecraft.getInstance().getItemRenderer().renderStatic(stacks.get(stack.getTag().get("disc")),
						TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
				matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), 90, true));
				matrixStack.translate(0.25, -0.07, 0.25);
				if (!stacks.containsKey(stack.getTag().get("driver"))) {
					stacks.put((CompoundNBT) stack.getTag().get("driver"),
							ItemStack.of((CompoundNBT) stack.getTag().get("driver")));
				}
				Minecraft.getInstance().getItemRenderer().renderStatic(stacks.get(stack.getTag().get("driver")),
						TransformType.FIRST_PERSON_LEFT_HAND, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
			}
		});

		matrixStack.popPose();
	}

}
