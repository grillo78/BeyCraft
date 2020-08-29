package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.LauncherContainer;
import com.grillo78.beycraft.inventory.slots.BeyLoggerContainer;
import com.grillo78.beycraft.items.ItemBeyLogger;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageUpdateUrlBeyLogger;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author a19guillermong
 *
 */
public class BeyloggerGUI extends ContainerScreen<BeyLoggerContainer> {

	private ItemStack beyLogger;
	private TextFieldWidget urlText;
	private Button setUrlBtn;

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public BeyloggerGUI(BeyLoggerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
		if (Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemBeyLogger) {
			beyLogger = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND);
		} else {
			beyLogger = Minecraft.getInstance().player.getHeldItem(Hand.OFF_HAND);
		}
		if (!beyLogger.hasTag()) {
			CompoundNBT compound = new CompoundNBT();
			beyLogger.setTag(compound);
		}

		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;

		setUrlBtn = new Button(relX + 9, relY + 40, 50, 20, new TranslationTextComponent("gui.done"), (Button) -> {
			beyLogger.getTag().putString("url", urlText.getText());
			PacketHandler.instance.sendToServer(new MessageUpdateUrlBeyLogger(urlText.getText()));
			Minecraft.getInstance().displayGuiScreen(null);
		});
		this.addButton(setUrlBtn);

		urlText = new TextFieldWidget(font, relX + 10, relY + 25, 155, 10, new StringTextComponent(""));
		urlText.setMaxStringLength(Integer.MAX_VALUE);
		children.add(urlText);
		if (beyLogger.getTag().contains("url")) {
			urlText.setText(beyLogger.getTag().getString("url"));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
		this.font.func_238422_b_(p_230451_1_, new StringTextComponent("URL:").func_241878_f(), xSize/2, ySize-155.0F, 4210752);
	}

	@Override
	public void tick() {
		super.tick();
		urlText.tick();
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(p_230430_1_);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
		urlText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/beylogger.png"));
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
	}
}
