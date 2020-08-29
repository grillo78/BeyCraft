package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.blocks.RobotBlock;
import com.grillo78.beycraft.inventory.BeyCreatorContainer;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageBeyCreatorUpdate;
import com.grillo78.beycraft.network.message.MessageSetRobotLevel;
import com.grillo78.beycraft.tileentity.RobotTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.Message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
public class RobotGUI extends Screen {

	private TextFieldWidget numberText;

	/** The X size of the inventory window in pixels. */
	protected int xSize = 176;
	/** The Y size of the inventory window in pixels. */
	protected int ySize = 166;
	private RayTraceResult rayTraceBlock = Minecraft.getInstance().player.pick(20.0D, 0.0F, false);
	private BlockPos pos = ((BlockRayTraceResult) rayTraceBlock).getPos();
	private RobotTileEntity tileEntity;

	/**
	 * @param titleIn
	 */
	public RobotGUI(ITextComponent titleIn, RobotTileEntity tileEntity) {
		super(titleIn);
		this.tileEntity = tileEntity;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected void init() {
		super.init();
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.addButton(new Button(relX + 55, relY + 15, 50, 20, new TranslationTextComponent("gui.done"), (Button) -> {
			try{
				PacketHandler.instance.sendToServer(new MessageSetRobotLevel(tileEntity.getPos(),Integer.valueOf(numberText.getText())));
				getMinecraft().displayGuiScreen(null);
			}catch (Exception e){
			}
		}));
		numberText = new TextFieldWidget(font, relX + 7, relY + 15, 45, 10,
				new StringTextComponent(""));
		numberText.setText(String.valueOf(tileEntity.getBladerLevel()));
		numberText.setMaxStringLength(Integer.MAX_VALUE);
		children.add(numberText);
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.renderBackground(p_230430_1_);
		this.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/robot.png"));
		this.blit(p_230430_1_, relX, relY, 0, 0, this.xSize, this.ySize);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		numberText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}
}
