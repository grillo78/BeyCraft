package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.LauncherContainer;
import com.grillo78.beycraft.items.ItemDualLauncher;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageUpdateColorLauncher;
import com.grillo78.beycraft.network.message.MessageUpdateDualLauncher;
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

/**
 * @author a19guillermong
 */
public class LauncherDualGUI extends ContainerScreen<LauncherContainer> {

	private Button rotationButton;
	private ItemStack launcher;
	private TextFieldWidget redText;
	private TextFieldWidget greenText;
	private TextFieldWidget blueText;
	private Button setColorBtn;

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public LauncherDualGUI(LauncherContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
		if (Minecraft.getInstance().player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemDualLauncher) {
			launcher = Minecraft.getInstance().player.getItemInHand(Hand.MAIN_HAND);
		} else {
			launcher = Minecraft.getInstance().player.getItemInHand(Hand.OFF_HAND);
		}
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		if (!launcher.hasTag() || !launcher.getTag().contains("rotation")) {
			PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(-1));
		}
		if (!launcher.hasTag()) {
			CompoundNBT compound = new CompoundNBT();
			compound.putInt("rotation", 1);
			launcher.setTag(compound);
		}
		if (!launcher.getTag().contains("rotation")) {
			CompoundNBT compound = launcher.getTag();
			compound.putInt("rotation", 1);
			launcher.setTag(compound);
		}
		if (launcher.getTag().getInt("rotation") == 1) {
			rotationButton = new Button(relX + 5, relY + 30, 30, 20, new StringTextComponent("Right"), (Button) -> {
				switch (launcher.getTag().getInt("rotation")) {
				case 1:
					PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(-1));
					launcher.getTag().putInt("rotation", -1);
					rotationButton.setMessage(new StringTextComponent("Left"));
					break;
				case -1:
					PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(1));
					launcher.getTag().putInt("rotation", 1);
					rotationButton.setMessage(new StringTextComponent("Right"));
					break;
				}
			});
		} else {

			rotationButton = new Button(relX + 5, relY + 30, 30, 20, new StringTextComponent("Left"), (Button) -> {
				switch (launcher.getTag().getInt("rotation")) {
				case 1:
					PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(-1));
					launcher.getTag().putInt("rotation", -1);
					rotationButton.setMessage(new StringTextComponent("Left"));
					break;
				case -1:
					PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(1));
					launcher.getTag().putInt("rotation", 1);
					rotationButton.setMessage(new StringTextComponent("Right"));
					break;
				}
			});
		}
		this.addButton(rotationButton);

		setColorBtn = new Button(relX + 5, relY + 53, 50, 20, new StringTextComponent("Set color"), (Button) -> {
			try {
				PacketHandler.instance
						.sendToServer(new MessageUpdateColorLauncher(Float.valueOf(redText.getValue()) / 255f,
								Float.valueOf(greenText.getValue()) / 255f, Float.valueOf(blueText.getValue()) / 255f));
			} catch (NumberFormatException e) {

			}
		});
		this.addButton(setColorBtn);

		redText = new TextFieldWidget(font, relX + 100, relY + 10, 50, 10, new StringTextComponent(""));
		children.add(redText);
		greenText = new TextFieldWidget(font, relX + 100, relY + 25, 50, 10, new StringTextComponent(""));
		children.add(greenText);
		blueText = new TextFieldWidget(font, relX + 100, relY + 45, 50, 10, new StringTextComponent(""));
		if (!launcher.getTag().contains("color")) {
			redText.setValue("255.0");
			greenText.setValue("255.0");
			blueText.setValue("255.0");
		} else {
			redText.setValue(String.valueOf(launcher.getTag().getCompound("color").getFloat("red") * 255));
			greenText.setValue(String.valueOf(launcher.getTag().getCompound("color").getFloat("green") * 255));
			blueText.setValue(String.valueOf(launcher.getTag().getCompound("color").getFloat("blue") * 255));
		}
		children.add(blueText);
	}

	@Override
	protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
		this.font.draw(p_230451_1_, this.inventory.getDisplayName().getVisualOrderText(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
		this.font.draw(p_230451_1_, new StringTextComponent("R:").getVisualOrderText(), imageWidth/2, imageHeight-155.0F, 4210752);
		this.font.draw(p_230451_1_, new StringTextComponent("G:").getVisualOrderText(), imageWidth/2, imageHeight-140.0F, 4210752);
		this.font.draw(p_230451_1_, new StringTextComponent("B:").getVisualOrderText(), imageWidth/2, imageHeight-125.0F, 4210752);
	}

	@Override
	public void tick() {
		super.tick();
		redText.tick();
		greenText.tick();
		blueText.tick();
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(p_230430_1_);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
		redText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		greenText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		blueText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager()
				.bind(new ResourceLocation(Reference.MODID, "textures/gui/container/launcher.png"));
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
	}
}
