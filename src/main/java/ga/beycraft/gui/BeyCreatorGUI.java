package ga.beycraft.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.Message;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageBeyCreatorUpdate;
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
	private BlockPos pos = ((BlockRayTraceResult) rayTraceBlock).getBlockPos();

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
				if (!BeyCraftRegistry.ITEMSLAYER.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyCraftRegistry.ITEMSLAYER.size() - 1;
					}
				}
				break;
			case 1:
				if (!BeyCraftRegistry.ITEMSDISCLIST.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyCraftRegistry.ITEMSDISCLIST.size() - 1;
					}
				}
				break;
			case 2:
				if (!BeyCraftRegistry.ITEMSDRIVER.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyCraftRegistry.ITEMSDRIVER.size() - 1;
					}
				}
				break;

			case 3:
				if (!BeyCraftRegistry.ITEMSFRAME.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyCraftRegistry.ITEMSFRAME.size() - 1;
					}
				}
				break;
			case 4:
				if (!BeyCraftRegistry.ITEMSGTCHIP.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyCraftRegistry.ITEMSGTCHIP.size() - 1;
					}
				}
				break;
			case 5:
				if (!BeyCraftRegistry.ITEMSGTWEIGHT.isEmpty()) {
					partCount--;
					if (partCount == -1) {
						partCount = BeyCraftRegistry.ITEMSGTWEIGHT.size() - 1;
					}
				}
				break;
			}
		});
		nextPart = new Button(relX + 71, relY + 5, 10, 20, new StringTextComponent(">"), (Button) -> {
			switch (partType) {
			case 0:
				if (!BeyCraftRegistry.ITEMSLAYER.isEmpty()) {
					partCount++;
					if (partCount == BeyCraftRegistry.ITEMSLAYER.size()) {
						partCount = 0;
					}
				}
				break;
			case 1:
				if (!BeyCraftRegistry.ITEMSDISCLIST.isEmpty()) {
					partCount++;
					if (partCount == BeyCraftRegistry.ITEMSDISCLIST.size()) {
						partCount = 0;
					}
				}
				break;
			case 2:
				if (!BeyCraftRegistry.ITEMSDRIVER.isEmpty()) {
					partCount++;
					if (partCount == BeyCraftRegistry.ITEMSDRIVER.size()) {
						partCount = 0;
					}
				}
				break;

			case 3:
				if (!BeyCraftRegistry.ITEMSFRAME.isEmpty()) {
					partCount++;
					if (partCount == BeyCraftRegistry.ITEMSFRAME.size()) {
						partCount = 0;
					}
				}
				break;

			case 4:
				if (!BeyCraftRegistry.ITEMSGTCHIP.isEmpty()) {
					partCount++;
					if (partCount == BeyCraftRegistry.ITEMSGTCHIP.size()) {
						partCount = 0;
					}
				}
				break;
			case 5:
				if (!BeyCraftRegistry.ITEMSGTWEIGHT.isEmpty()) {
					partCount++;
					if (partCount == BeyCraftRegistry.ITEMSGTWEIGHT.size()) {
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
				if (!BeyCraftRegistry.ITEMSLAYER.isEmpty()) {
					stack = new ItemStack(BeyCraftRegistry.ITEMSLAYER.get(partCount));
				}
				break;
			case 1:
				if (!BeyCraftRegistry.ITEMSDISCLIST.isEmpty()) {
					stack = new ItemStack(BeyCraftRegistry.ITEMSDISCLIST.get(partCount));
				}
				break;
			case 2:
				if (!BeyCraftRegistry.ITEMSDRIVER.isEmpty()) {
					stack = new ItemStack(BeyCraftRegistry.ITEMSDRIVER.get(partCount));
				}
				break;

			case 3:
				if (!BeyCraftRegistry.ITEMSFRAME.isEmpty()) {
					stack = new ItemStack(BeyCraftRegistry.ITEMSFRAME.get(partCount));
				}
				break;

			case 4:
				if (!BeyCraftRegistry.ITEMSGTCHIP.isEmpty()) {
					stack = new ItemStack(BeyCraftRegistry.ITEMSGTCHIP.get(partCount));
				}
				break;

			case 5:
				if (!BeyCraftRegistry.ITEMSGTWEIGHT.isEmpty()) {
					stack = new ItemStack(BeyCraftRegistry.ITEMSGTWEIGHT.get(partCount));
				}
				break;
			}
			if (stack != null)
				PacketHandler.instance.sendToServer(new MessageBeyCreatorUpdate(stack, pos));
			getMinecraft().setScreen(null);
		}));
		this.addButton(nextPartType);
		this.addButton(prevPart);
		this.addButton(nextPart);
	}

	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager()
				.bind(new ResourceLocation(Reference.MOD_ID, "textures/gui/container/empty_container.png"));
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
		ITextComponent partName = TextComponentUtils.fromMessage(new Message() {

			@Override
			public String getString() {
				switch (partType) {
				case 0:
					if (!BeyCraftRegistry.ITEMSLAYER.isEmpty()) {
						return BeyCraftRegistry.ITEMSLAYER.get(partCount).getDescription().getString();
					}
					return "";
				case 1:
					if (!BeyCraftRegistry.ITEMSDISCLIST.isEmpty()) {
						return BeyCraftRegistry.ITEMSDISCLIST.get(partCount).getDescription().getString();
					}
					return "";
				case 2:
					if (!BeyCraftRegistry.ITEMSDRIVER.isEmpty()) {
						return BeyCraftRegistry.ITEMSDRIVER.get(partCount).getDescription().getString();
					}
					return "";
				case 3:
					if (!BeyCraftRegistry.ITEMSFRAME.isEmpty()) {
						return BeyCraftRegistry.ITEMSFRAME.get(partCount).getDescription().getString();
					}
					return "";
				case 4:
					if (!BeyCraftRegistry.ITEMSGTCHIP.isEmpty()) {
						return BeyCraftRegistry.ITEMSGTCHIP.get(partCount).getDescription().getString();
					}
					return "";
				case 5:
					if (!BeyCraftRegistry.ITEMSGTWEIGHT.isEmpty()) {
						return BeyCraftRegistry.ITEMSGTWEIGHT.get(partCount).getDescription().getString();
					}
					return "";
				}
				return "";
			}
		});
		Style sPartName = partName.getStyle();
		sPartName.withColor(Color.fromLegacyFormat(TextFormatting.BLACK));

		switch (partType) {
		case 0:
			if (!BeyCraftRegistry.ITEMSLAYER.isEmpty()) {
				ItemStack stack = new ItemStack(BeyCraftRegistry.ITEMSLAYER.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 1:
			if (!BeyCraftRegistry.ITEMSDISCLIST.isEmpty()) {
				ItemStack stack = new ItemStack(BeyCraftRegistry.ITEMSDISCLIST.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 2:
			if (!BeyCraftRegistry.ITEMSDRIVER.isEmpty()) {
				ItemStack stack = new ItemStack(BeyCraftRegistry.ITEMSDRIVER.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 3:
			if (!BeyCraftRegistry.ITEMSFRAME.isEmpty()) {
				ItemStack stack = new ItemStack(BeyCraftRegistry.ITEMSFRAME.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 4:
			if (!BeyCraftRegistry.ITEMSGTCHIP.isEmpty()) {
				ItemStack stack = new ItemStack(BeyCraftRegistry.ITEMSGTCHIP.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 5:
			if (!BeyCraftRegistry.ITEMSGTWEIGHT.isEmpty()) {
				ItemStack stack = new ItemStack(BeyCraftRegistry.ITEMSGTWEIGHT.get(partCount));
				renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		}
		this.font.draw(matrixStack, partName.getVisualOrderText(), relX + 4, relY + 30, 4210752);
	}

	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.drawGuiContainerBackgroundLayer(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}


	protected void renderItemIntoGUI(ItemStack stack, int x, int y) {
		IBakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(stack, (World)null, (LivingEntity)null);
		RenderSystem.pushMatrix();
		Minecraft.getInstance().getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS);
		Minecraft.getInstance().getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS).setFilter(false, false);
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableAlphaTest();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.translatef((float)x, (float)y, 100.0F + Minecraft.getInstance().getItemRenderer().blitOffset);
		RenderSystem.translatef(8.0F, 8.0F, 0.0F);
		RenderSystem.scalef(1.0F, -1.0F, 1.0F);
		RenderSystem.scalef(16.0F, 16.0F, 16.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.pushPose();
		matrixstack.scale(7,7,7);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
		boolean flag = !bakedmodel.usesBlockLight();
		if (flag) {
			RenderHelper.setupForFlatItems();
		}

		Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
		irendertypebuffer$impl.endBatch();
		RenderSystem.enableDepthTest();
		if (flag) {
			RenderHelper.setupFor3DItems();
		}

		matrixstack.popPose();
		RenderSystem.disableAlphaTest();
		RenderSystem.disableRescaleNormal();
		RenderSystem.popMatrix();
	}
}
