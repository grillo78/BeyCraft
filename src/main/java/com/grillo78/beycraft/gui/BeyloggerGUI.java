package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.LauncherContainer;
import com.grillo78.beycraft.inventory.slots.BeyLoggerContainer;
import com.grillo78.beycraft.items.ItemBeyLogger;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageUpdateUrlBeyLogger;
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

		setUrlBtn = new Button(relX + 9, relY + 40, 50, 20, "Set URL", (Button) -> {
			beyLogger.getTag().putString("url", urlText.getText());
			PacketHandler.instance.sendToServer(new MessageUpdateUrlBeyLogger(urlText.getText()));
			minecraft.displayGuiScreen(null);
		});
		this.addButton(setUrlBtn);

		urlText = new TextFieldWidget(font, relX + 10, relY + 25, 155, 10, "");
		urlText.setMaxStringLength(Integer.MAX_VALUE);
		children.add(urlText);
		if (beyLogger.getTag().contains("url")) {
			urlText.setText(beyLogger.getTag().getString("url"));
		}
	}

	@Override
	public void tick() {
		urlText.tick();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.urlText.render(mouseX, mouseY, partialTicks);
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.font.drawString("URL:", relX + 10.0F, relY + 10.0F, 4210752);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.getTextureManager()
				.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/beylogger.png"));
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(relX, relY, 0, 0, this.xSize, this.ySize);

	}
}
