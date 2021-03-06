package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageBeyCreatorUpdate;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

/**
 * @author grillo78
 */
public class BeyCreatorGUI extends Screen {

	private int partCount = 0;
	private int partType = 0;
	private Button prevPart;
	private Button nextPart;
	private Button nextPartType;
	protected int xSize = 176;
	protected int ySize = 166;
	private RayTraceResult rayTraceBlock = Minecraft.getInstance().player.pick(20.0D, 0.0F, false);
	private BlockPos pos = ((BlockRayTraceResult) rayTraceBlock).getPos();

	public BeyCreatorGUI(ITextComponent titleIn) {
		super(titleIn);
	}

	@Override
	protected void init() {
		super.init();
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		prevPart = new Button(relX + 5, relY + 5, 10, 20, new StringTextComponent("<"), (Button) -> {
			switch (partType) {
			case 0:
				if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyRegistry.ITEMSLAYER.size() - 1;
					}
				}
				break;
			case 1:
				if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyRegistry.ITEMSDISCLIST.size() - 1;
					}
				}
				break;
			case 2:
				if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyRegistry.ITEMSDRIVER.size() - 1;
					}
				}
				break;

			case 3:
				if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyRegistry.ITEMSFRAME.size() - 1;
					}
				}
				break;
			case 4:
				if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyRegistry.ITEMSGTCHIP.size() - 1;
					}
				}
				break;
			case 5:
				if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyRegistry.ITEMSGTWEIGHT.size() - 1;
					}
				}
				break;
			}
		});
		nextPart = new Button(relX + 71, relY + 5, 10, 20, new StringTextComponent(">"), (Button) -> {
			switch (partType) {
			case 0:
				if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
					partCount++;
					if (partCount == BeyRegistry.ITEMSLAYER.size()) {
						partCount = 0;
					}
				}
				break;
			case 1:
				if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
					partCount++;
					if (partCount == BeyRegistry.ITEMSDISCLIST.size()) {
						partCount = 0;
					}
				}
				break;
			case 2:
				if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
					partCount++;
					if (partCount == BeyRegistry.ITEMSDRIVER.size()) {
						partCount = 0;
					}
				}
				break;

			case 3:
				if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
					partCount++;
					if (partCount == BeyRegistry.ITEMSFRAME.size()) {
						partCount = 0;
					}
				}
				break;

			case 4:
				if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
					partCount++;
					if (partCount == BeyRegistry.ITEMSGTCHIP.size()) {
						partCount = 0;
					}
				}
				break;
			case 5:
				if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
					partCount++;
					if (partCount == BeyRegistry.ITEMSGTWEIGHT.size()) {
						partCount = 0;
					}
				}
				break;
			}
		});
		nextPartType = new Button(relX + 15, relY + 5, 56, 20, new StringTextComponent("Layers"), (Button) -> {
			partType++;
			partCount = 0;
			if (partType == 6) {
				partType = 0;
			}
			switch (partType) {
			case 0:
				nextPartType.setMessage(new StringTextComponent("Layers"));
				break;
			case 1:
				nextPartType.setMessage(new StringTextComponent("Discs"));
				break;
			case 2:
				nextPartType.setMessage(new StringTextComponent("Drivers"));
				break;
			case 3:
				nextPartType.setMessage(new StringTextComponent("Frames"));
				break;
			case 4:
				nextPartType.setMessage(new StringTextComponent("GT Chips"));
				break;
			case 5:
				nextPartType.setMessage(new StringTextComponent("GT Weights"));
				break;
			}
		});
		this.addButton(new Button(relX + 5, relY + ySize-26, 54, 20, new StringTextComponent("Accept"), (Button) -> {
			ItemStack stack = null;
			switch (partType) {
			case 0:
				if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
					stack = new ItemStack(BeyRegistry.ITEMSLAYER.get(partCount));
				}
				break;
			case 1:
				if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
					stack = new ItemStack(BeyRegistry.ITEMSDISCLIST.get(partCount));
				}
				break;
			case 2:
				if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
					stack = new ItemStack(BeyRegistry.ITEMSDRIVER.get(partCount));
				}
				break;

			case 3:
				if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
					stack = new ItemStack(BeyRegistry.ITEMSFRAME.get(partCount));
				}
				break;

			case 4:
				if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
					stack = new ItemStack(BeyRegistry.ITEMSGTCHIP.get(partCount));
				}
				break;

			case 5:
				if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
					stack = new ItemStack(BeyRegistry.ITEMSGTWEIGHT.get(partCount));
				}
				break;
			}
			if (stack != null)
				PacketHandler.instance.sendToServer(new MessageBeyCreatorUpdate(stack, pos));
			getMinecraft().displayGuiScreen(null);
		}));
		this.addButton(nextPartType);
		this.addButton(prevPart);
		this.addButton(nextPart);
	}

	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/empty_container.png"));
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
		ITextComponent partName = TextComponentUtils.toTextComponent(new Message() {

			@Override
			public String getString() {
				switch (partType) {
				case 0:
					if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
						return BeyRegistry.ITEMSLAYER.get(partCount).getName().getString();
					}
					return "";
				case 1:
					if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
						return BeyRegistry.ITEMSDISCLIST.get(partCount).getName().getString();
					}
					return "";
				case 2:
					if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
						return BeyRegistry.ITEMSDRIVER.get(partCount).getName().getString();
					}
					return "";
				case 3:
					if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
						return BeyRegistry.ITEMSFRAME.get(partCount).getName().getString();
					}
					return "";
				case 4:
					if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
						return BeyRegistry.ITEMSGTCHIP.get(partCount).getName().getString();
					}
					return "";
				case 5:
					if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
						return BeyRegistry.ITEMSGTWEIGHT.get(partCount).getName().getString();
					}
					return "";
				}
				return "";
			}
		});
		Style sPartName = partName.getStyle();
		sPartName.setColor(Color.func_240744_a_(TextFormatting.BLACK));

		switch (partType) {
		case 0:
			if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSLAYER.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 1:
			if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSDISCLIST.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 2:
			if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSDRIVER.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 3:
			if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSFRAME.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 4:
			if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSGTCHIP.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 5:
			if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSGTWEIGHT.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		}
		this.font.func_238422_b_(matrixStack, partName.func_241878_f(), relX + 4, relY + 30, 4210752);
	}

	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.drawGuiContainerBackgroundLayer(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}


	protected void renderItemIntoGUI(ItemStack stack, int x, int y) {
		IBakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, (World)null, (LivingEntity)null);
		RenderSystem.pushMatrix();
		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getInstance().getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableAlphaTest();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.translatef((float)x, (float)y, 100.0F + Minecraft.getInstance().getItemRenderer().zLevel);
		RenderSystem.translatef(8.0F, 8.0F, 0.0F);
		RenderSystem.scalef(1.0F, -1.0F, 1.0F);
		RenderSystem.scalef(16.0F, 16.0F, 16.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.push();
		matrixstack.scale(7,7,7);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		boolean flag = !bakedmodel.func_230044_c_();
		if (flag) {
			RenderHelper.setupGuiFlatDiffuseLighting();
		}

		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
		irendertypebuffer$impl.finish();
		RenderSystem.enableDepthTest();
		if (flag) {
			RenderHelper.setupGui3DDiffuseLighting();
		}

		matrixstack.pop();
		RenderSystem.disableAlphaTest();
		RenderSystem.disableRescaleNormal();
		RenderSystem.popMatrix();
	}
}
