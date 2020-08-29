package com.grillo78.beycraft.inventory;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.entity.player.BeltModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class ItemBeltProvider implements ICapabilityProvider, ICapabilitySerializable {

	private final LazyOptional<IItemHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(2));
	private final ItemStack stack;
	private final LazyOptional<ICurio> curio = LazyOptional.of(() -> new ICurio() {
		private BeltModel model = new BeltModel();
		private final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/belt/belt.png");

		@Override
		public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
			return true;
		}

		@Override
		public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
				int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks,
				float ageInTicks, float netHeadYaw, float headPitch) {
			matrixStack.push();
			ICurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
			ICurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);

			IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, model.getRenderType(TEXTURE), false,
					false);
			model.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

			if (stack.hasTag() && stack.getTag().contains("launcher")) {
				matrixStack.translate(0.4, 0.6, -0.1);
				matrixStack.rotate(new Quaternion(new Vector3f(0, 0, 1), -90, true));
				matrixStack.rotate(new Quaternion(new Vector3f(0, 1, 0), 180, true));
				matrixStack.scale(0.2f, 0.2f, 0.2f);
				Minecraft.getInstance().getItemRenderer().renderItem(
						ItemStack.read((CompoundNBT) stack.getTag().get("launcher")),
						ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrixStack,
						renderTypeBuffer);
			}
			matrixStack.pop();
		}
	});

	public ItemBeltProvider(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		if (cap == CuriosCapability.ITEM) {
			return curio.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public INBT serializeNBT() {
		return inventory.map(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(items, null))
				.orElseGet(CompoundNBT::new);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		inventory.ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, nbt));
	}

}
