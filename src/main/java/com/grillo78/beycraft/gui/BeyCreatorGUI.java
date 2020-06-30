package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.BeyCreatorContainer;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageBeyCreatorUpdate;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

/**
 * @author a19guillermong
 */
public class BeyCreatorGUI extends ContainerScreen<BeyCreatorContainer> {

	private int partCount = 0;
	private int partType = 0;
	private Button prevPart;
	private Button nextPart;
	private Button nextPartType;
	private RayTraceResult rayTraceBlock = Minecraft.getInstance().player.pick(20.0D, 0.0F, false);
	private BlockPos pos = ((BlockRayTraceResult) rayTraceBlock).getPos();

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public BeyCreatorGUI(BeyCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void func_231160_c_() {
		super.func_231160_c_();
		int relX = (this.field_230708_k_ - this.xSize) / 2;
		int relY = (this.field_230709_l_ - this.ySize) / 2;
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
				nextPartType.func_238482_a_(new StringTextComponent("Layers"));
				break;
			case 1:
				nextPartType.func_238482_a_(new StringTextComponent("Discs"));
				break;
			case 2:
				nextPartType.func_238482_a_(new StringTextComponent("Drivers"));
				break;
			case 3:
				nextPartType.func_238482_a_(new StringTextComponent("Frames"));
				break;
			case 4:
				nextPartType.func_238482_a_(new StringTextComponent("GT Chips"));
				break;
			case 5:
				nextPartType.func_238482_a_(new StringTextComponent("GT Weights"));
				break;
			}
		});
		this.func_230480_a_(new Button(relX + 5, relY + ySize-26, 54, 20, new StringTextComponent("Accept"), (Button) -> {
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
		this.func_230480_a_(nextPartType);
		this.func_230480_a_(prevPart);
		this.func_230480_a_(nextPart);
	}

//	@Override
//	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
//		this.renderBackground();
//		super.render(p_render_1_, p_render_2_, p_render_3_);
//	}

	@Override
	protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/empty_container.png"));
		int relX = (this.field_230708_k_ - this.xSize) / 2;
		int relY = (this.field_230709_l_ - this.ySize) / 2;
		this.func_238474_b_(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
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
		sPartName.func_240712_a_(TextFormatting.BLACK);
		switch (partType) {
		case 0:
			if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSLAYER.get(partCount));
				Minecraft.getInstance()
						.getItemRenderer().renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 1:
			if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSDISCLIST.get(partCount));
				Minecraft.getInstance()
						.getItemRenderer().renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 2:
			if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSDRIVER.get(partCount));
				Minecraft.getInstance()
						.getItemRenderer().renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 3:
			if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSFRAME.get(partCount));
				Minecraft.getInstance()
						.getItemRenderer().renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 4:
			if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSGTCHIP.get(partCount));
				Minecraft.getInstance()
						.getItemRenderer().renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		case 5:
			if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
				ItemStack stack = new ItemStack(BeyRegistry.ITEMSGTWEIGHT.get(partCount));
				Minecraft.getInstance()
						.getItemRenderer().renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15);
			}
			break;
		}
//		this.field_230712_o_.renderString(partName, relX + (getXSize()
//				- Minecraft.getInstance().fontRenderer.getStringWidth(partName.getString())) / 2f, relY + 30, 50, false, );
	}
}
